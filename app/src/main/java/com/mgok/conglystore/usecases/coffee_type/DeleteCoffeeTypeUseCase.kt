package com.mgok.conglystore.usecases.coffee_type

import com.mgok.conglystore.data.remote.coffee.CoffeeRemoteRepository
import javax.inject.Inject

class DeleteCoffeeTypeUseCase @Inject constructor(
    private val coffeeRemoteRepository: CoffeeRemoteRepository
) {
    suspend fun delete(coffeeType: String) =  coffeeRemoteRepository.deleteCoffeeByType(coffeeType)
}