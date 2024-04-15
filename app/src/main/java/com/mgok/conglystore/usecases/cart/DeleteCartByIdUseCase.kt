package com.mgok.conglystore.usecases.cart

import com.mgok.conglystore.data.remote.cart.CartRemoteRepository
import javax.inject.Inject

class DeleteCartByIdUseCase @Inject constructor(
    private val cartRemoteRepository: CartRemoteRepository
) {
    suspend fun delete(cartId: String) = cartRemoteRepository.delete(cartId)
}