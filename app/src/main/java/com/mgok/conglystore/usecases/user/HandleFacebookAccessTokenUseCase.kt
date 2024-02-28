package com.mgok.conglystore.usecases.user

import com.facebook.AccessToken
import com.mgok.conglystore.data.remote.user.UserRemoteRepository
import javax.inject.Inject

class HandleFacebookAccessTokenUseCase @Inject constructor(
    private val userRemoteRepository: UserRemoteRepository
) {
    suspend fun handleFacebookAccessToken(token: AccessToken) =
        userRemoteRepository.handleFacebookAccessToken(token)
}