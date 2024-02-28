package com.mgok.conglystore.data.remote.user

import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import com.facebook.AccessToken

interface UserRemoteRepository {

    suspend fun getInfoUser(): User?

    suspend fun signOut()

    suspend fun onSignInResult(intent: Intent)

    suspend fun loginWithAccount(email: String, password: String)

    suspend fun googleAuthUiClientSignIn(): IntentSender?

    suspend fun handleFacebookAccessToken(token: AccessToken)

    suspend fun createAccount(email: String, password: String)

    suspend fun sendRequestResetPassword(email: String)

    suspend fun updateAvatarWithLinkOther(imageUrl: String):Uri

    suspend fun updateAvatar(uri: Uri):Uri

    suspend fun deleteAvatar(uri: Uri)

    suspend fun createInfoUser(user: User)


}