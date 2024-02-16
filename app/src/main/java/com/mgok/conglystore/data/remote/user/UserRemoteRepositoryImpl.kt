package com.mgok.conglystore.data.remote.user

import android.content.Intent
import android.net.Uri
import android.util.Log
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.mgok.conglystore.Session.getUserSession
import com.mgok.conglystore.presentation.auth.ResultStatusState
import com.mgok.conglystore.presentation.auth.reset_password.ResetPasswordState
import com.mgok.conglystore.presentation.auth.sign_in.GoogleAuthUiClient
import com.mgok.conglystore.presentation.auth.sign_in.SignInState
import com.mgok.conglystore.presentation.auth.sign_up.SignUpState
import com.mgok.conglystore.presentation.user.UpdateInfoUserState
import com.mgok.conglystore.presentation.user.UploadState
import kotlinx.coroutines.tasks.await
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject


class UserRemoteRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val googleAuthUiClient: GoogleAuthUiClient,
    private val storage: FirebaseStorage
) : UserRemoteRepository {


    override suspend fun getInfoUser(): User? {
        return try {
            val snapshot = firestore.collection("users").document(auth.uid.toString()).get().await()
            if (!snapshot.exists()) {
                throw NullPointerException()
            }
            snapshot.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun signOut() {
        auth.signOut()
        googleAuthUiClient.signOut()
        if (AccessToken.getCurrentAccessToken() != null) {
            GraphRequest(
                AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE,
                {
                    AccessToken.setCurrentAccessToken(null)
                    LoginManager.getInstance().logOut()
                }
            ).executeAsync()
        }
    }

    override fun userCurrent() = auth.currentUser

    override suspend fun onSignInResult(intent: Intent): SignInState {
        return googleAuthUiClient.signInWithIntent(intent = intent)
    }


    override suspend fun loginWithAccount(email: String, password: String): SignInState {
        return try {
            val task = auth.signInWithEmailAndPassword(email, password).await()
            if (task.user == null) throw Exception()
            SignInState(
                status = ResultStatusState.Successful,
                error = null
            )
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            SignInState(
                status = ResultStatusState.Error,
                error = "Email hoặc mật khẩu không chính xác"
            )
        } catch (e: IllegalArgumentException) {
            SignInState(
                status = ResultStatusState.Error,
                error = "Không để trống thông tin"
            )
        } catch (e: Exception) {
            SignInState(
                status = ResultStatusState.Error,
                error = "Đã xảy ra lỗi"
            )
        }
    }


    override suspend fun googleAuthUiClientSignIn() = googleAuthUiClient.signIn()


    override suspend fun handleFacebookAccessToken(token: AccessToken): SignInState {
        return try {
            val credential = FacebookAuthProvider.getCredential(token.token)
            val authResult = auth.signInWithCredential(credential).await()
            if (authResult.user == null) throw Exception()
            SignInState(
                status = ResultStatusState.Successful,
                error = null
            )
        } catch (e: FirebaseAuthUserCollisionException) {
            auth.signOut()
            SignInState(
                status = ResultStatusState.Error,
                error = e.message
            )
        } catch (e: Exception) {
            auth.signOut()
            SignInState(
                status = ResultStatusState.Error,
                error = e.message
            )
        }
    }


    override suspend fun createAccount(email: String, password: String): SignUpState {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            SignUpState(
                status = ResultStatusState.Successful,
                error = null
            )
        } catch (e: FirebaseAuthWeakPasswordException) {
            SignUpState(
                status = ResultStatusState.Error,
                error = "Mật khẩu nhiều hơn 5 kí tự"
            )
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            SignUpState(
                status = ResultStatusState.Error,
                error = "Email không hợp lệ"
            )
        } catch (e: IllegalArgumentException) {
            SignUpState(
                status = ResultStatusState.Error,
                error = "Hãy điền đầy đủ thông tin"
            )
        } catch (e: FirebaseAuthUserCollisionException) {
            SignUpState(
                status = ResultStatusState.Error,
                error = "Email đã được đăng ký"
            )
        }
    }


    override suspend fun sendRequestRestPassword(email: String): ResetPasswordState {
        return try {
            auth.sendPasswordResetEmail(email).await()
            ResetPasswordState(
                status = ResultStatusState.Successful,
                message = "Kiểm tra email của bạn"
            )
        } catch (e: Exception) {
            ResetPasswordState(
                status = ResultStatusState.Error,
                message = "Đã xảy ra lỗi"
            )
        }
    }

    override suspend fun updateAvatarWithLinkOther(imageUrl: String, uid: String): UploadState {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(imageUrl)
            .build()
        return try {
            val response = client.newCall(request).execute()
            if (!response.isSuccessful || response.body == null) throw Exception()
            val imageData = response.body!!.bytes()
            val avatarRef = storage.reference
                .child("avatars/$uid.png")
            val uploadTask = avatarRef.putBytes(imageData).await()
            val url = uploadTask.storage.downloadUrl.await()
            UploadState(
                status = ResultStatusState.Successful,
                error = null,
                url = url
            )
        } catch (e: Exception) {
            UploadState(
                status = ResultStatusState.Successful,
                error = e.message,
                url = null
            )
        }
    }

    override suspend fun updateAvatar(uri: Uri, uid: String): UploadState {
        return try {
            val avatarRef = storage.reference
                .child("avatars/$uid.png")
            val uploadTask = avatarRef.putFile(uri).await()
            val url = uploadTask.storage.downloadUrl.await()
            UploadState(
                status = ResultStatusState.Successful,
                error = null,
                url = url
            )
        } catch (e: Exception) {
            UploadState(
                status = ResultStatusState.Successful,
                error = e.message,
                url = null
            )
        }
    }

    override suspend fun deleteAvatar(uri: Uri) {
        try {
            storage.getReferenceFromUrl(uri.toString()).delete().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun createInfoUser(user: User): UpdateInfoUserState {
        return try {
            firestore.collection("users")
                .document(user.uid)
                .set(user).await()
            UpdateInfoUserState(status = ResultStatusState.Successful)
        } catch (e: Exception) {
            Log.e("ghg createInfoUser", e.toString())
            UpdateInfoUserState(status = ResultStatusState.Error, error = "Đã xảy ra lỗi")
        }
    }

    override suspend fun addItemFavorite(idCoffee: String) {
        try {
            val item = mapOf(
                idCoffee to idCoffee
            )
            val map = mapOf(
                "favorites" to FieldValue.arrayUnion(idCoffee)
            )
            firestore.collection("users").document(getUserSession()?.uid.toString())
                .set(map, SetOptions.merge())
        } catch (e: Exception) {
            Log.e("ghg addItemFavorite: ", e.toString())
        }
    }

    override suspend fun removeItemFavorite(idCoffee: String) {
        try {
            val map = mapOf(
                "favorites" to FieldValue.arrayRemove(idCoffee)
            )
            firestore.collection("users").document(getUserSession()?.uid.toString())
                .set(map, SetOptions.merge())
        } catch (e: Exception) {
            Log.e("ghg removeItemFavorite: ", e.toString())
        }
    }
}