package com.mgok.conglystore.usecases.coffee

import com.mgok.conglystore.data.remote.coffee.CoffeeRemoteRepository
import javax.inject.Inject

class GetCoffeeByIdUseCase @Inject constructor(
    private val coffeeRemoteRepository: CoffeeRemoteRepository
) {
    suspend fun getById(coffeeId: String) = coffeeRemoteRepository.getById(coffeeId)
}