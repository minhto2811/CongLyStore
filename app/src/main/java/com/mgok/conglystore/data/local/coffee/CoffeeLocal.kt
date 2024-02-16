package com.mgok.conglystore.data.local.coffee

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mgok.conglystore.data.remote.coffee.Coffee
import com.mgok.conglystore.utilities.toListObject

@Entity(tableName = "coffees")
data class CoffeeLocal(
    @PrimaryKey
    val id: Long = System.currentTimeMillis(),
    val name: String = "",
    val type: String = "",
    val image: String = "",
    val vote: Float = 4.8f,
    val sizes: String = "",
    val description: String = "",
) {
    fun toCoffee(): Coffee {
        return Coffee(
            id = this.id,
            name = this.name,
            type = this.type,
            image = this.image,
            vote = this.vote,
            sizes = this.sizes.toListObject(),
            description = this.description
        )
    }

}



