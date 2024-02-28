package com.mgok.conglystore.usecases.user

import com.mgok.conglystore.data.remote.user.UserRemoteRepository
import javax.inject.Inject

class LoginWithAccountUseCase @Inject constructor(
    private val userRemoteRepository: UserRemoteRepository
) {

    suspend fun loginWithAccount(email: String, password: String) =
        userRemoteRepository.loginWithAccount(email, password)
}