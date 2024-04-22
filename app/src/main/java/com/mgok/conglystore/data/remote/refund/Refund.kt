package com.mgok.conglystore.data.remote.refund

import java.util.Date
import java.util.UUID

data class Refund(
    val id: String = UUID.randomUUID().toString(),
    val userId: String = "",
    val billId: String = "",
    val percent: Float = 0f,
    val status: Int = -1,
    val time: Long = Date().time
)
