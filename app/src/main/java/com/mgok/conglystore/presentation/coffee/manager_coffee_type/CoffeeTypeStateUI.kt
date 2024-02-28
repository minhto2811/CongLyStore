package com.mgok.conglystore.presentation.coffee.manager_coffee_type

import com.mgok.conglystore.data.remote.coffee_type.CoffeeType

data class CoffeeTypeStateUI(
    val listCoffeeType: List<CoffeeType> = listOf(),
    val loading: Boolean = false
)
