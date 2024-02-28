package com.mgok.conglystore.usecases.coffee_type

import com.mgok.conglystore.data.remote.coffee_type.CoffeeTypeRemoteRepository
import javax.inject.Inject

class GetListCoffeeTypeUseCase @Inject constructor(
    private val coffeeTypeRemoteRepository: CoffeeTypeRemoteRepository
) {
    suspend operator fun invoke() = coffeeTypeRemoteRepository.getListCoffeeType()
}