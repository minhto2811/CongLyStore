package com.mgok.conglystore.usecases.user

import com.mgok.conglystore.data.remote.user.UserRemoteRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val userRemoteRepository: UserRemoteRepository
) {

    suspend fun sendRequestResetPassword(email: String) =
        userRemoteRepository.sendRequestResetPassword(email)
}