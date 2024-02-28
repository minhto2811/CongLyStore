package com.mgok.conglystore.usecases.image

import android.net.Uri
import com.mgok.conglystore.data.remote.coffee.CoffeeRemoteRepository
import javax.inject.Inject

class DeleteImageUseCase @Inject constructor(
    private val coffeeRemoteRepository: CoffeeRemoteRepository
) {
    suspend fun deleteImage(uri: Uri) = coffeeRemoteRepository.deleteImage(uri)
}