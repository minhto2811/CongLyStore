package com.mgok.conglystore.usecases.coffee

import com.mgok.conglystore.data.remote.coffee.Coffee
import com.mgok.conglystore.data.remote.coffee.CoffeeRemoteRepository
import javax.inject.Inject

class InsertCoffeeUseCase @Inject constructor(
    private val coffeeRemoteRepository: CoffeeRemoteRepository
) {
    suspend fun insertCoffee(coffee: Coffee) = coffeeRemoteRepository.insertCoffee(coffee)
}