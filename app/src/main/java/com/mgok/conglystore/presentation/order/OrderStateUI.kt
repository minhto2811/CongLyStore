package com.mgok.conglystore.presentation.order

import com.mgok.conglystore.data.remote.address.Address
import com.mgok.conglystore.data.remote.cart.Cart

data class OrderStateUI(
    val loading: Boolean = false,
    val listCart: List<Cart> = listOf(),
    val totalPrice: Float = 0f,
    val address: Address? = null
)
