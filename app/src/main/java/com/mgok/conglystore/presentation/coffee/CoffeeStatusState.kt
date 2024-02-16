package com.mgok.conglystore.presentation.coffee

import com.mgok.conglystore.data.local.coffee.CoffeeType
import com.mgok.conglystore.data.remote.coffee.Coffee
import com.mgok.conglystore.presentation.auth.ResultStatusState
import com.mgok.conglystore.presentation.user.UploadState

data class CoffeeStatusState(
    val status: ResultStatusState = ResultStatusState.Default,
    val listCoffeeType: List<CoffeeType> = emptyList(),
    val listCoffee: List<Coffee> = emptyList(),
    val coffeeSelected: Coffee? = null,
    val uploadState: UploadState = UploadState()
)
