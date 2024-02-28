package com.mgok.conglystore.presentation.home.tabs.tab_cart

import com.mgok.conglystore.data.remote.cart.Cart

data class CartStateUI(
    val loading: Boolean = false,
    val listCart: List<Cart> = listOf(),
    val totalPrice:Float = 0f
)
