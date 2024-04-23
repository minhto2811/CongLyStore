package com.mgok.conglystore.usecases.coffee

import com.mgok.conglystore.data.remote.coffee.CoffeeRemoteRepository
import javax.inject.Inject

class FilterCoffeebyQuerryUseCase @Inject constructor(
    private val coffeeRemoteRepository: CoffeeRemoteRepository
) {
    suspend fun filter(querry: String) = coffeeRemoteRepository.filterCoffeeByQuery(querry)
}