package com.mgok.conglystore.usecases.favorite

import com.mgok.conglystore.data.remote.favorite.FavoriteRemoteRepository
import javax.inject.Inject

class DeteleFavoriteUseCase @Inject constructor(
    private val favoriteRemoteRepository: FavoriteRemoteRepository
) {
    fun delete(coffeeId: String) = favoriteRemoteRepository.removeItemFavorite(coffeeId)
}