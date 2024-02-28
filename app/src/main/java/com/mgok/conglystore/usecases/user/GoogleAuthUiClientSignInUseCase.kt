package com.mgok.conglystore.usecases.user

import com.mgok.conglystore.data.remote.user.UserRemoteRepository
import javax.inject.Inject

class GoogleAuthUiClientSignInUseCase @Inject constructor(
    private val userRemoteRepository: UserRemoteRepository
) {
    suspend operator fun invoke() = userRemoteRepository.googleAuthUiClientSignIn()
}