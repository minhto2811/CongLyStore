package com.mgok.conglystore.data.local.coffee

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coffee_types")
data class CoffeeType(
    @PrimaryKey
    val id: Long = System.currentTimeMillis(),
    val name: String = "",
)
