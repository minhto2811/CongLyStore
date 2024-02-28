package com.mgok.conglystore.data.remote.cart

interface CartRemoteRepository {

    suspend fun getListCart(): List<Cart>

    suspend fun upsertCart(cart: Cart)

    suspend fun delete(cartId: String)
}