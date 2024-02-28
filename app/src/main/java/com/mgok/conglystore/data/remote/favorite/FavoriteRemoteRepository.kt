package com.mgok.conglystore.data.remote.favorite

interface FavoriteRemoteRepository {

    suspend fun getListFavourite():List<String>?

     fun addItemFavorite(coffeeId: String)

     fun removeItemFavorite(coffeeId: String)
}