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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mgok.conglystore.presentation.auth.sign_in.GoogleAuthUiClient
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
            Log.e("getInfoUser: ", snapshot.toObject(User::class.java).toString())
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


    override suspend fun onSignInResult(intent: Intent) =
        googleAuthUiClient.signInWithIntent(intent = intent)


    override suspend fun loginWithAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }


    override suspend fun googleAuthUiClientSignIn() = googleAuthUiClient.signIn()


    override suspend fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential).await()
    }


    override suspend fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
    }


    override suspend fun sendRequestResetPassword(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    override suspend fun updateAvatarWithLinkOther(imageUrl: String): Uri {
        val client = OkHttpClient()
        val request = Request.Builder().url(imageUrl).build()
        val response = client.newCall(request).execute()
        if (!response.isSuccessful || response.body == null) throw Exception()
        val imageData = response.body!!.bytes()
        val avatarRef = storage.reference
            .child("avatars/${auth.uid}.png")
        val uploadTask = avatarRef.putBytes(imageData).await()
        return uploadTask.storage.downloadUrl.await()
    }

    override suspend fun updateAvatar(uri: Uri): Uri {
        val avatarRef = storage.reference
            .child("avatars/${auth.uid}.png")
        val uploadTask = avatarRef.putFile(uri).await()
        return uploadTask.storage.downloadUrl.await()
    }

    override suspend fun deleteAvatar(uri: Uri) {
        storage.getReferenceFromUrl(uri.toString()).delete().await()
    }

    override suspend fun createInfoUser(user: User) {
        firestore.collection("users")
            .document(user.uid)
            .set(user).await()
    }


}