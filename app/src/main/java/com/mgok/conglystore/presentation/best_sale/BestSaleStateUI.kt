package com.mgok.conglystore.presentation.best_sale

import com.mgok.conglystore.data.remote.coffee.Coffee

data class BestSaleStateUI(
    val loading: Boolean = false,
    val message: String? = null,
    val list: List<Coffee> = listOf()
)
