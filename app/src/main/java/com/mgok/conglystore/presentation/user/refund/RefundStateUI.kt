package com.mgok.conglystore.presentation.user.refund

import com.mgok.conglystore.data.remote.refund.Refund

data class RefundStateUI(
    val loading: Boolean = false,
    val message: String = "",
    val list: List<Refund> = listOf()
)
