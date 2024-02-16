package com.mgok.conglystore.data.local.coffee

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface CoffeeDAO {
    @Query("SELECT * FROM coffees")
    suspend fun getList(): List<CoffeeLocal>

    @Upsert
    suspend fun insertAll(list: List<CoffeeLocal>)

    @Upsert
    suspend fun insert(coffeeLocal: CoffeeLocal)

    @Query("DELETE FROM coffees")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(coffeeLocal: CoffeeLocal)
}