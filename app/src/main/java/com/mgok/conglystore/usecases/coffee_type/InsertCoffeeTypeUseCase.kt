package com.mgok.conglystore.usecases.coffee_type

import com.mgok.conglystore.data.remote.coffee_type.CoffeeType
import com.mgok.conglystore.data.remote.coffee_type.CoffeeTypeRemoteRepository
import javax.inject.Inject

class InsertCoffeeTypeUseCase @Inject constructor(
    private val coffeeTypeRemoteRepository: CoffeeTypeRemoteRepository
) {
    suspend fun insertCoffeeType(coffeeType: CoffeeType) = coffeeTypeRemoteRepository.insertCoffeType(coffeeType)
}