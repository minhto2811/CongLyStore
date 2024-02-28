package com.mgok.conglystore.data.remote.coffee_type

interface CoffeeTypeRemoteRepository {
    suspend fun insertCoffeType(coffeeType: CoffeeType)

    suspend fun getListCoffeeType(): List<CoffeeType>

    suspend fun deleteCoffeeType(id: String)
}