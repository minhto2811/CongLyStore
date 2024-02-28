package com.mgok.conglystore.usecases.user

import android.content.Intent
import com.mgok.conglystore.data.remote.user.UserRemoteRepository
import javax.inject.Inject

class SignInResultUseCase @Inject constructor(
    private val userRemoteRepository: UserRemoteRepository
) {
    suspend fun onSignIn(intent: Intent) = userRemoteRepository.onSignInResult(intent)
}