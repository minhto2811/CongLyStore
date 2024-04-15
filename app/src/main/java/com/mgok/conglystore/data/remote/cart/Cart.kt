package com.mgok.conglystore.data.remote.cart

import com.mgok.conglystore.data.remote.coffee.Coffee
import java.util.UUID

data class Cart(
    val id: String = UUID.randomUUID().toString(),
    val userId: String = "",
    val idCoffee: String = "",
    var quantity: Int = 1,
    var size: String = "",
    var coffee: Coffee? = null
)
