package com.mgok.conglystore.usecases.user

import com.mgok.conglystore.data.remote.user.UserRemoteRepository
import javax.inject.Inject

class CreateAccountUseCase @Inject constructor(
    private val userRemoteRepository: UserRemoteRepository
) {
    suspend fun createAccount(email: String, password: String) = userRemoteRepository.createAccount(email, password)
}