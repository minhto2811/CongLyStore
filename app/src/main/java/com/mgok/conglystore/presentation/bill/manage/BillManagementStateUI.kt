package com.mgok.conglystore.presentation.bill.manage

import com.mgok.conglystore.data.remote.bill.Bill

data class BillManagementStateUI(
    val loading: Boolean = false,
    val list: List<Bill> = listOf(),
    val message: String? = null
)
