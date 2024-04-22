package com.mgok.conglystore.usecases.coffee

import com.mgok.conglystore.data.remote.coffee.CoffeeRemoteRepository
import javax.inject.Inject

class UpdateSoldProductUseCase @Inject constructor(
    private val coffeeRemoteRepository: CoffeeRemoteRepository
) {

    suspend operator fun invoke(idCoffee: String, sold: Int) =
        coffeeRemoteRepository.updateSold(idCoffee, sold)
}