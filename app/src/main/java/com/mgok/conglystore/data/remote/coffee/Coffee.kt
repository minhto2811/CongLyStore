package com.mgok.conglystore.data.remote.coffee

import java.util.UUID


data class Coffee(
    var id: String = UUID.randomUUID().toString(),
    var name: String = "",
    val type: String = "",
    var image: String? = null,
    val vote: Float = 4.8f,
    val sizes: List<Size> = listOf(),
    val description: String = "A cappuccino is an approximately 150 ml (5 oz) beverage, with 25 ml of espresso coffee and 85ml of fresh milk.",
    var delete: Boolean = false,
    val sold: Int = 0
)

data class Size(
    val size: String = "M",
    val price: Float = 3f
)

