package com.mgok.conglystore.presentation.bill.detail

import com.mgok.conglystore.data.remote.bill.Bill

data class BillStateUI(
    val loading: Boolean = false,
    val bill: Bill? = null,
    val message: String? = null,
    val role: Int = 1
)
