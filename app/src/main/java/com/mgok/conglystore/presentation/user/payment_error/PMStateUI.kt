package com.mgok.conglystore.presentation.user.payment_error

import com.mgok.conglystore.data.remote.error_payment.ErrorPayment

data class PMStateUI(
    val loading: Boolean = false,
    val message: String? = null,
    val list: List<ErrorPayment> = listOf(),
    val role: Int = 1
)
