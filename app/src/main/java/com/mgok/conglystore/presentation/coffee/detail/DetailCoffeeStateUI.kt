package com.mgok.conglystore.presentation.coffee.detail

import com.mgok.conglystore.data.remote.coffee.Coffee
import com.mgok.conglystore.data.remote.coffee.Size

data class DetailCoffeeStateUI(
    val coffee: Coffee? = null,
    val loading: Boolean = false,
    val isFavorite: Boolean = false,
    val size: Size? = null,
)
