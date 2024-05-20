package com.mgok.conglystore.usecases.coffee_type

import com.mgok.conglystore.data.remote.coffee_type.CoffeeTypeRemoteRepository
import javax.inject.Inject

class DeleteCoffeeTypeUseCase @Inject constructor(
    private val coffeeTypeRemoteRepository: CoffeeTypeRemoteRepository
) {
    suspend fun delete(coffeeType: String) =  coffeeTypeRemoteRepository.deleteCoffeeType(coffeeType)
}