package com.mgok.conglystore.usecases.coffee

import com.mgok.conglystore.data.remote.coffee.CoffeeRemoteRepository
import javax.inject.Inject

class GetListCoffeeBySold @Inject constructor(
    private val coffeeRemoteRepository: CoffeeRemoteRepository
) {

    suspend operator fun invoke() = coffeeRemoteRepository.getListCoffeeBySold()
}