package com.mgok.conglystore.data.remote.error_payment

import java.util.Date
import java.util.UUID

data class ErrorPayment(
    val id: String = UUID.randomUUID().toString(),
    val userId: String = "",
    val billId: String = "",
    val status: Int = -1,
    val time: Long = Date().time
)


//-1 cho xu li
//0 da xu li
//1 tu choi
