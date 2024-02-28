package com.mgok.conglystore.presentation.home.tabs.tab_home

import com.mgok.conglystore.data.remote.coffee.Coffee
import com.mgok.conglystore.data.remote.coffee_type.CoffeeType
import com.mgok.conglystore.data.remote.user.User

data class HomeStateUI(
    val listCoffeeType: List<CoffeeType> = listOf(),
    val listCoffee: List<Coffee> = listOf(),
    val location: String = "Chưa bật vị trí",
    val user: User? = null
)
