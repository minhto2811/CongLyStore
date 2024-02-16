package com.mgok.conglystore.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mgok.conglystore.data.local.coffee.CoffeeDAO
import com.mgok.conglystore.data.local.coffee.CoffeeLocal
import com.mgok.conglystore.data.local.coffee.CoffeeType
import com.mgok.conglystore.data.local.coffee.CoffeeTypeDAO

@Database(entities = [CoffeeType::class, CoffeeLocal::class], version = 1, exportSchema = false)
abstract class MyDatabase() : RoomDatabase() {

    abstract fun coffeeTypeDao(): CoffeeTypeDAO
    abstract fun coffeeDao(): CoffeeDAO
}