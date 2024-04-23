package com.mgok.conglystore.presentation.coffee.manager_product

import com.mgok.conglystore.data.remote.coffee.Coffee

data class ProductManagementStateUI(
    val loading:Boolean = false,
    val message:String? = null,
    val list:List<Coffee> = listOf()
)
