package com.mgok.conglystore.usecases.favorite

import com.mgok.conglystore.data.remote.favorite.FavoriteRemoteRepository
import javax.inject.Inject

class GetListFavoriteUseCase @Inject constructor(
    private val favoriteRemoteRepository: FavoriteRemoteRepository
) {
    suspend fun getList() = favoriteRemoteRepository.getListFavourite()
}