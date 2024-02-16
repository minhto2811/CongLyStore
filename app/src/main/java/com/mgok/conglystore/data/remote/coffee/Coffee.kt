package com.mgok.conglystore.data.remote.coffee

import com.mgok.conglystore.data.local.coffee.CoffeeLocal
import org.json.JSONArray


data class Coffee(
    val id: Long = System.currentTimeMillis(),
    val name: String = "",
    val type: String = "",
    val image: String = "",
    val vote: Float = 4.8f,
    val sizes: List<Size> = listOf(
        Size("S", 2f),
        Size(),
        Size("L", 4f)
    ),
    val description: String = "A cappuccino is an approximately 150 ml (5 oz) beverage, with 25 ml of espresso coffee and 85ml of fresh milk.",
) {
    fun toCoffeeLocal(): CoffeeLocal {
        return CoffeeLocal(
            id = this.id,
            name = this.name,
            type = this.type,
            image = this.image,
            vote = this.vote,
            sizes = JSONArray(this.sizes).toString(),
            description = this.description
        )
    }
}

data class Size(
    val size: String = "M",
    val price: Float = 3f
)

