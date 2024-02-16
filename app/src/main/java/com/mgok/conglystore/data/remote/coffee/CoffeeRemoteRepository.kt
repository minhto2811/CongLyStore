package com.mgok.conglystore.data.remote.coffee

import android.net.Uri
import com.mgok.conglystore.data.local.coffee.CoffeeType
import com.mgok.conglystore.presentation.auth.ResultStatusState
import com.mgok.conglystore.presentation.coffee.CoffeeStatusState
import com.mgok.conglystore.presentation.user.UploadState

interface CoffeeRemoteRepository {

    suspend fun insertCoffeType(coffeeType: CoffeeType): ResultStatusState

    suspend fun getListCoffeeType(): CoffeeStatusState

    suspend fun deleteCoffeeType(id: String): ResultStatusState

    suspend fun insertCoffee(coffee: Coffee): ResultStatusState
    suspend fun getListCoffee(): CoffeeStatusState
    suspend fun uploadImage(uri: Uri, uid: String): UploadState

    suspend fun deleteImage(uri: Uri)
}