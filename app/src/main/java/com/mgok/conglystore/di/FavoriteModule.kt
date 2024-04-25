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
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object FavoriteModule {

    @Provides
    @ViewModelScoped
    fun providesFavoriteRemoteRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): FavoriteRemoteRepository {
        return FavoriteRemoteRepositoryImpl(firestore, auth)
    }


    @Provides
    @ViewModelScoped
    fun providesGetListFavoriteUseCase(favoriteRemoteRepository: FavoriteRemoteRepository): GetListFavoriteUseCase {
        return GetListFavoriteUseCase(favoriteRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesAddFavoriteUseCase(favoriteRemoteRepository: FavoriteRemoteRepository): AddFavoriteUseCase {
        return AddFavoriteUseCase(favoriteRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesDeteleFavoriteUseCase(favoriteRemoteRepository: FavoriteRemoteRepository): DeteleFavoriteUseCase {
        return DeteleFavoriteUseCase(favoriteRemoteRepository)
    }

}