package com.mgok.conglystore.usecases.user

import android.net.Uri
import com.mgok.conglystore.data.remote.user.UserRemoteRepository
import javax.inject.Inject

class DeleteAvatarUseCase @Inject constructor(
    private val userRemoteRepository: UserRemoteRepository
) {
    suspend fun deleteAvatar(uri: Uri) = userRemoteRepository.deleteAvatar(uri)
}