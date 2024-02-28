package com.mgok.conglystore.usecases.image

import android.net.Uri
import com.mgok.conglystore.data.remote.coffee.CoffeeRemoteRepository
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(
    private val coffeeRemoteRepository: CoffeeRemoteRepository
) {
    suspend fun uploadImage(uri: Uri, uid: String) = coffeeRemoteRepository.uploadImage(uri, uid)
}