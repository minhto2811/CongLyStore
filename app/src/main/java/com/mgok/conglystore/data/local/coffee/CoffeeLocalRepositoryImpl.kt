package com.mgok.conglystore.data.local.coffee

import com.mgok.conglystore.data.local.MyDatabase
import javax.inject.Inject

class CoffeeLocalRepositoryImpl @Inject constructor(
    private val myDatabase: MyDatabase
) : CoffeeLocalRepository {
    override suspend fun insertAllCoffeeType(list: List<CoffeeType>) {
        myDatabase.coffeeTypeDao().insertAll(list)
    }

    override suspend fun insertCoffeeType(coffeeType: CoffeeType) {
        myDatabase.coffeeTypeDao().insert(coffeeType)
    }

    override suspend fun getListCoffeeType(): List<CoffeeType> {
        return myDatabase.coffeeTypeDao().getList()
    }

    override suspend fun delete(coffeeType: CoffeeType) {
        myDatabase.coffeeTypeDao().delete(coffeeType)
    }

    override suspend fun deleteCoffeeType(coffeeType: CoffeeType) {
        myDatabase.coffeeTypeDao().deleteAll()
    }

    override suspend fun deleteAllCoffeeType() {
        myDatabase.coffeeTypeDao().deleteAll()
    }

    override suspend fun insertCoffee(coffeeLocal: CoffeeLocal) {
        myDatabase.coffeeDao().insert(coffeeLocal)
    }

    override suspend fun insertAllCoffee(list: List<CoffeeLocal>) {
       myDatabase.coffeeDao().insertAll(list)
    }

    override suspend fun getListCoffee(): List<CoffeeLocal> {
        return myDatabase.coffeeDao().getList()
    }

}