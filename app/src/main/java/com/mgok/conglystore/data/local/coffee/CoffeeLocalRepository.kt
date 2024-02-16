package com.mgok.conglystore.data.local.coffee

interface CoffeeLocalRepository {
    suspend fun insertAllCoffeeType(list: List<CoffeeType>)

    suspend fun insertCoffeeType(coffeeType: CoffeeType)

    suspend fun getListCoffeeType(): List<CoffeeType>

    suspend fun delete(coffeeType: CoffeeType)

    suspend fun deleteCoffeeType(coffeeType: CoffeeType)

    suspend fun deleteAllCoffeeType()

    suspend fun insertCoffee(coffeeLocal: CoffeeLocal)

    suspend fun insertAllCoffee(list: List<CoffeeLocal>)

    suspend fun getListCoffee(): List<CoffeeLocal>

}