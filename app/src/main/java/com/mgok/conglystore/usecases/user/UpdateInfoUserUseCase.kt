package com.mgok.conglystore.usecases.user

import com.mgok.conglystore.data.remote.user.User
import com.mgok.conglystore.data.remote.user.UserRemoteRepository
import javax.inject.Inject

class UpdateInfoUserUseCase @Inject constructor(
    private val userRemoteRepository: UserRemoteRepository
) {
    suspend fun createInfoUser(user: User) = userRemoteRepository.createInfoUser(user)
}