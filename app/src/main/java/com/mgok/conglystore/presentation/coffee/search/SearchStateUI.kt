package com.mgok.conglystore.presentation.coffee.search

import com.mgok.conglystore.data.remote.coffee.Coffee
import com.mgok.conglystore.data.remote.coffee_type.CoffeeType

data class SearchStateUI(
    val listCoffeeType: List<CoffeeType> = listOf(),
    val listCoffee: List<Coffee> = listOf(),
    val loading: Boolean = false
)
