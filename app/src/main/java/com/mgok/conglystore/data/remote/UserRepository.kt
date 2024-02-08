package com.mgok.conglystore.data.remote

import android.content.Intent
import com.facebook.AccessToken
import com.google.firebase.Firebase
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.mgok.conglystore.presentation.auth.GoogleAuthUiClient
import com.mgok.conglystore.presentation.auth.reset_password.ResetPasswordState
import com.mgok.conglystore.presentation.auth.sign_in.SignInState
import com.mgok.conglystore.presentation.auth.sign_up.SignUpState
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val googleAuthUiClient: GoogleAuthUiClient
) {


    suspend fun checkNewUser(callback: (Boolean) -> Unit) {
        try {
            val snapshot = firestore.collection("users")
                .whereEqualTo("uid", auth.uid)
                .limit(1)
                .get()
                .await()
            callback.invoke(snapshot.isEmpty)
        } catch (e: Exception) {
            callback.invoke(true)
        }
    }

    suspend fun signOut() {
        auth.signOut()
        googleAuthUiClient.signOut()
    }

    fun userCurrent() = auth.currentUser

    suspend fun onSignInResult(intent: Intent): SignInState {
        val signInResult = googleAuthUiClient.signInWithIntent(intent = intent)
        return SignInState(
            isSignInSuccessful = signInResult.data != null,
            signInError = signInResult.errorMessage
        )
    }


    suspend fun loginWithAccount(email: String, password: String): SignInState {
        return try {
            val task = auth.signInWithEmailAndPassword(email, password).await()
            SignInState(
                isSignInSuccessful = task.user != null,
                signInError = null
            )
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            SignInState(
                isSignInSuccessful = false,
                signInError = "Email hoặc mật khẩu không chính xác"
            )
        } catch (e: IllegalArgumentException) {
            SignInState(
                isSignInSuccessful = false,
                signInError = "Không để trống thông tin"
            )
        } catch (e: Exception) {
            SignInState(
                isSignInSuccessful = false,
                signInError = "Đã xảy ra lỗi"
            )
        }
    }


    suspend fun googleAuthUiClientSignIn() = googleAuthUiClient.signIn()


    suspend fun handleFacebookAccessToken(token: AccessToken): SignInState {
        return try {
            val credential = FacebookAuthProvider.getCredential(token.token)
            val authResult = Firebase.auth.signInWithCredential(credential).await()
            SignInState(
                isSignInSuccessful = authResult.user != null,
                signInError = null
            )
        } catch (e: FirebaseAuthUserCollisionException) {
            auth.signOut()
            SignInState(
                isSignInSuccessful = false,
                signInError = e.message
            )
        }
    }


    suspend fun createAccount(email: String, password: String): SignUpState {
        return try {
            val task = auth.createUserWithEmailAndPassword(email, password).await()
            SignUpState(
                isSignUpSuccessful = task.user != null,
                signUpError = null
            )
        } catch (e: FirebaseAuthWeakPasswordException) {
            SignUpState(
                isSignUpSuccessful = false,
                signUpError = "Mật khẩu nhiều hơn 5 kí tự"
            )
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            SignUpState(
                isSignUpSuccessful = false,
                signUpError = "Email không hợp lệ"
            )
        } catch (e: IllegalArgumentException) {
            SignUpState(
                isSignUpSuccessful = false,
                signUpError = "Hãy điền đầy đủ thông tin"
            )
        } catch (e: FirebaseAuthUserCollisionException) {
            SignUpState(
                isSignUpSuccessful = false,
                signUpError = "Email đã được đăng ký"
            )
        }
    }


    suspend fun sendRequestRestPassword(email: String): ResetPasswordState {
        return try {
            auth.sendPasswordResetEmail(email).await()
            ResetPasswordState(
                isSuccessful = true,
                message = "Kiểm tra email của bạn"
            )
        } catch (e: Exception) {
            ResetPasswordState(
                isSuccessful = false,
                message = "Đã xảy ra lỗi"
            )
        }
    }
}