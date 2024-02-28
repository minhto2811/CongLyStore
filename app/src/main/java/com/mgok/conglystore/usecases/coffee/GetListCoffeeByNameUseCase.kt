package com.mgok.conglystore.usecases.coffee

import com.mgok.conglystore.data.remote.coffee.CoffeeRemoteRepository
import javax.inject.Inject

class GetListCoffeeByNameUseCase @Inject constructor(
    private val coffeeRemoteRepository: CoffeeRemoteRepository
) {
    suspend fun getListCoffeeByName(coffeeName:String) = coffeeRemoteRepository.getListCoffeeByName(coffeeName)
}