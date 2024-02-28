package com.mgok.conglystore.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mgok.conglystore.data.remote.favorite.FavoriteRemoteRepository
import com.mgok.conglystore.data.remote.favorite.FavoriteRemoteRepositoryImpl
import com.mgok.conglystore.usecases.favorite.AddFavoriteUseCase
import com.mgok.conglystore.usecases.favorite.DeteleFavoriteUseCase
import com.mgok.conglystore.usecases.favorite.GetListFavoriteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FavoriteModule {

    @Provides
    @Singleton
    fun providesFavoriteRemoteRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): FavoriteRemoteRepository {
        return FavoriteRemoteRepositoryImpl(firestore, auth)
    }


    @Provides
    @Singleton
    fun providesGetListFavoriteUseCase(favoriteRemoteRepository: FavoriteRemoteRepository): GetListFavoriteUseCase {
        return GetListFavoriteUseCase(favoriteRemoteRepository)
    }

    @Provides
    @Singleton
    fun providesAddFavoriteUseCase(favoriteRemoteRepository: FavoriteRemoteRepository): AddFavoriteUseCase {
        return AddFavoriteUseCase(favoriteRemoteRepository)
    }

    @Provides
    @Singleton
    fun providesDeteleFavoriteUseCase(favoriteRemoteRepository: FavoriteRemoteRepository): DeteleFavoriteUseCase {
        return DeteleFavoriteUseCase(favoriteRemoteRepository)
    }

}