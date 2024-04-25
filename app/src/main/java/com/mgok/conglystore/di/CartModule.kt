package com.mgok.conglystore.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mgok.conglystore.data.remote.cart.CartRemoteRepository
import com.mgok.conglystore.data.remote.cart.CartRemoteRepositoryImpl
import com.mgok.conglystore.usecases.cart.DeleteAllBillUseCase
import com.mgok.conglystore.usecases.cart.DeleteCartByIdUseCase
import com.mgok.conglystore.usecases.cart.GetListCartUseCase
import com.mgok.conglystore.usecases.cart.UpsertCartUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object CartModule {
    @Provides
    @ViewModelScoped
    fun providesCartRemoteRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): CartRemoteRepository {
        return CartRemoteRepositoryImpl(firestore, auth)
    }

    @Provides
    @ViewModelScoped
    fun providesUpsertCartUseCase(cartRemoteRepository: CartRemoteRepository): UpsertCartUseCase {
        return UpsertCartUseCase(cartRemoteRepository)
    }


    @Provides
    @ViewModelScoped
    fun providesGetListCartUseCase(cartRemoteRepository: CartRemoteRepository): GetListCartUseCase {
        return GetListCartUseCase(cartRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesDeleteCartByIdUseCase(cartRemoteRepository: CartRemoteRepository): DeleteCartByIdUseCase {
        return DeleteCartByIdUseCase(cartRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesDeleteAllBillUseCase(cartRemoteRepository: CartRemoteRepository): DeleteAllBillUseCase {
        return DeleteAllBillUseCase(cartRemoteRepository)
    }

}