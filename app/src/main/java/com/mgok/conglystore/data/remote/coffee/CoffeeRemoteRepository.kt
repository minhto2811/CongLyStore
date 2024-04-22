package com.mgok.conglystore.data.remote.coffee

import android.net.Uri

interface CoffeeRemoteRepository {
    suspend fun getListCoffee(): List<Coffee>

    suspend fun getById(idCoffee: String): Coffee?
    suspend fun insertCoffee(coffee: Coffee)
    suspend fun getListCoffeeByName(coffeeName: String): List<Coffee>
    suspend fun uploadImage(uri: Uri, uid: String): Uri

    suspend fun deleteImage(uri: String)

    suspend fun updateSold(idCoffee: String, sold: Int)

    suspend fun getListCoffeeBySold(): List<Coffee>
}