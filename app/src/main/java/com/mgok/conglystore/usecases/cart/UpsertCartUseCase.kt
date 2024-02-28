package com.mgok.conglystore.usecases.cart

import com.mgok.conglystore.data.remote.cart.Cart
import com.mgok.conglystore.data.remote.cart.CartRemoteRepository
import javax.inject.Inject

class UpsertCartUseCase @Inject constructor(
    private val cartRemoteRepository: CartRemoteRepository
) {
    suspend fun upsertCart(cart: Cart) = cartRemoteRepository.upsertCart(cart)
}