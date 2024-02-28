package com.mgok.conglystore.presentation.home.tabs.tab_fav

import com.mgok.conglystore.data.remote.coffee.Coffee

data class FavoriteStateUI(
    val loading: Boolean = false,
    val listCoffee: List<Coffee> = listOf()
)
