package com.mgok.conglystore.data.remote.user

import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import com.facebook.AccessToken
import com.google.firebase.auth.FirebaseUser
import com.mgok.conglystore.presentation.auth.reset_password.ResetPasswordState
import com.mgok.conglystore.presentation.auth.sign_in.SignInState
import com.mgok.conglystore.presentation.auth.sign_up.SignUpState
import com.mgok.conglystore.presentation.user.UpdateInfoUserState
import com.mgok.conglystore.presentation.user.UploadState

interface UserRemoteRepository {

    suspend fun getInfoUser(): User?

    suspend fun signOut()

    fun userCurrent(): FirebaseUser?

    suspend fun onSignInResult(intent: Intent): SignInState

    suspend fun loginWithAccount(email: String, password: String): SignInState

    suspend fun googleAuthUiClientSignIn(): IntentSender?

    suspend fun handleFacebookAccessToken(token: AccessToken): SignInState

    suspend fun createAccount(email: String, password: String): SignUpState

    suspend fun sendRequestRestPassword(email: String): ResetPasswordState

    suspend fun updateAvatarWithLinkOther(imageUrl: String, uid: String): UploadState

    suspend fun updateAvatar(uri: Uri, uid: String): UploadState

    suspend fun deleteAvatar(uri: Uri)

    suspend fun createInfoUser(user: User): UpdateInfoUserState

    suspend fun addItemFavorite(idCoffee: String)

    suspend fun removeItemFavorite(idCoffee: String)
}