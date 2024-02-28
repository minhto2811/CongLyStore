package com.mgok.conglystore.data.remote.address

import java.util.UUID

data class Address(
    val id: String = UUID.randomUUID().toString(),
    val userId: String = "",
    val longtitue: Double = 0.0,
    val latitue: Double = 0.0,
    val displayName: String = "",
    val numberPhone: String = "",
    val location: String = ""
)
