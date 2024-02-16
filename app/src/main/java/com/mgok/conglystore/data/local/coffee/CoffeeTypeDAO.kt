package com.mgok.conglystore.data.local.coffee

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert


@Dao
interface CoffeeTypeDAO {
    @Query("SELECT * FROM coffee_types")
    suspend fun getList(): List<CoffeeType>

    @Upsert
    suspend fun insertAll(list: List<CoffeeType>)

    @Upsert
    suspend fun insert(coffeeType: CoffeeType)

    @Query("DELETE FROM coffee_types")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(coffeeType: CoffeeType)
}