package com.mgok.conglystore.usecases.coffee

import com.mgok.conglystore.data.remote.coffee.CoffeeRemoteRepository
import javax.inject.Inject

class DeleteCoffeeByIdUseCase @Inject constructor(
    private val coffeeRemoteRepository: CoffeeRemoteRepository
) {
    suspend fun delete(coffeeId: String) = coffeeRemoteRepository.deleteCoffee(coffeeId)
}