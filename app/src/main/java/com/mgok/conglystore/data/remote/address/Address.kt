package com.mgok.conglystore.data.remote.address

import java.util.UUID

data class Address(
    val id: String = UUID.randomUUID().toString(),
    val userId: String = "",
    val displayName: String = "",
    val numberPhone: String = "",
    val location: String = ""
)
