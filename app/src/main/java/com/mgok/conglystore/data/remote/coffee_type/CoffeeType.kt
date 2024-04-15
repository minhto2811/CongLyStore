package com.mgok.conglystore.data.remote.coffee_type

import java.util.UUID

data class CoffeeType(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val delete: Boolean = false
)
