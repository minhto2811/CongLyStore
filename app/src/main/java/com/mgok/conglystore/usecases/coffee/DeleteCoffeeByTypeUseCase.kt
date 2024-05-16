package com.mgok.conglystore.usecases.coffee

import com.mgok.conglystore.data.remote.coffee.CoffeeRemoteRepository
import javax.inject.Inject

class DeleteCoffeeByTypeUseCase @Inject constructor(
    private val coffeeRepository: CoffeeRemoteRepository
) {
    suspend operator fun invoke(type: String) = coffeeRepository.deleteCoffeeByType(type)

}