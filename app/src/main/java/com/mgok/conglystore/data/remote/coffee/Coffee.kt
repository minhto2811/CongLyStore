package com.mgok.conglystore.data.remote.coffee

import java.util.UUID


data class Coffee(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val type: String = "",
    val image: String = "",
    val vote: Float = 4.8f,
    val sizes: List<Size> = listOf(
        Size("S", 2f),
        Size(),
        Size("L", 4f)
    ),
    val description: String = "A cappuccino is an approximately 150 ml (5 oz) beverage, with 25 ml of espresso coffee and 85ml of fresh milk.",
)

data class Size(
    val size: String = "M",
    val price: Float = 3f
)

