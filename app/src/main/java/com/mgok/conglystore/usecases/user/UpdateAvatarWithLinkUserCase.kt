package com.mgok.conglystore.usecases.user

import com.mgok.conglystore.data.remote.user.UserRemoteRepository
import javax.inject.Inject

class UpdateAvatarWithLinkUserCase @Inject constructor(
    private val userRemoteRepository: UserRemoteRepository
) {
    suspend fun updateAvatarWithLinkOther(imageUrl: String) =
        userRemoteRepository.updateAvatarWithLinkOther(imageUrl)
}