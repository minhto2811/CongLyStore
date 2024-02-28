package com.mgok.conglystore.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mgok.conglystore.data.remote.address.AddressRemoteRepository
import com.mgok.conglystore.data.remote.address.AddressRemoteRepositoryImpl
import com.mgok.conglystore.usecases.address.DeleteAddressUseCase
import com.mgok.conglystore.usecases.address.GetAddressUseCase
import com.mgok.conglystore.usecases.address.GetListAddressUseCase
import com.mgok.conglystore.usecases.address.UpsertAddressUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AddressModule {


    @Provides
    @Singleton
    fun providesAddressRemoteRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): AddressRemoteRepository {
        return AddressRemoteRepositoryImpl(auth, firestore)
    }

    @Provides
    @Singleton
    fun providesGetListAddressUseCase(addressRemoteRepository: AddressRemoteRepository): GetListAddressUseCase {
        return GetListAddressUseCase(addressRemoteRepository)
    }

    @Provides
    @Singleton
    fun providesGetAddressUseCase(addressRemoteRepository: AddressRemoteRepository): GetAddressUseCase {
        return GetAddressUseCase(addressRemoteRepository)
    }

    @Provides
    @Singleton
    fun providesUpsertAddressUseCase(addressRemoteRepository: AddressRemoteRepository): UpsertAddressUseCase {
        return UpsertAddressUseCase(addressRemoteRepository)
    }

    @Provides
    @Singleton
    fun providesDeleteAddressUseCase(addressRemoteRepository: AddressRemoteRepository): DeleteAddressUseCase {
        return DeleteAddressUseCase(addressRemoteRepository)
    }

}