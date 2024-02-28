package com.mgok.conglystore.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mgok.conglystore.data.remote.cart.CartRemoteRepository
import com.mgok.conglystore.data.remote.cart.CartRemoteRepositoryImpl
import com.mgok.conglystore.usecases.cart.DeleteCartUseCase
import com.mgok.conglystore.usecases.cart.GetListCartUseCase
import com.mgok.conglystore.usecases.cart.UpsertCartUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CartModule {
    @Provides
    @Singleton
    fun providesCartRemoteRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): CartRemoteRepository {
        return CartRemoteRepositoryImpl(firestore, auth)
    }

    @Provides
    @Singleton
    fun providesUpsertCartUseCase(cartRemoteRepository: CartRemoteRepository): UpsertCartUseCase {
        return UpsertCartUseCase(cartRemoteRepository)
    }


    @Provides
    @Singleton
    fun providesGetListCartUseCase(cartRemoteRepository: CartRemoteRepository): GetListCartUseCase {
        return GetListCartUseCase(cartRemoteRepository)
    }

    @Provides
    @Singleton
    fun providesDeleteCartUseCase(cartRemoteRepository: CartRemoteRepository): DeleteCartUseCase {
        return DeleteCartUseCase(cartRemoteRepository)
    }

}