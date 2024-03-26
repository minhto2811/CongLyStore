package com.mgok.conglystore.presentation.coffee.manager_product

import android.net.Uri
import com.mgok.conglystore.data.remote.coffee_type.CoffeeType

data class NewCoffeeStateUI(
    val loading: Boolean = false,
    val listCoffeeType: List<CoffeeType> = listOf(),
    val url: Uri? = null,
    val error: String? = null,
)