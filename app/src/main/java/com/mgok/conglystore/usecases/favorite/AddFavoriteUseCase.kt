package com.mgok.conglystore.usecases.favorite

import com.mgok.conglystore.data.remote.favorite.FavoriteRemoteRepository
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(
    private val favoriteRemoteRepository: FavoriteRemoteRepository
) {
    fun add(idCoffee: String) = favoriteRemoteRepository.addItemFavorite(idCoffee)
}