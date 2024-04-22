package com.mgok.conglystore.presentation.home.tabs.tab_history

import com.mgok.conglystore.data.remote.bill.Bill

data class HistoryStateUI(
    val loading: Boolean = false,
    val listBill: List<Bill> = listOf(),
    val message: String? = null
)
