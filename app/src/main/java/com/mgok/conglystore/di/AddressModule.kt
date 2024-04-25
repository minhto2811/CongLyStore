package com.mgok.conglystore.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mgok.conglystore.data.remote.address.AddressRemoteRepository
import com.mgok.conglystore.data.remote.address.AddressRemoteRepositoryImpl
import com.mgok.conglystore.usecases.address.DeleteAddressUseCase
import com.mgok.conglystore.usecases.address.GetAddressUseCase
import com.mgok.conglystore.usecases.address.GetFistAddressUseCase
import com.mgok.conglystore.usecases.address.GetListAddressUseCase
import com.mgok.conglystore.usecases.address.UpsertAddressUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AddressModule {


    @Provides
    @ViewModelScoped
    fun providesAddressRemoteRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): AddressRemoteRepository {
        return AddressRemoteRepositoryImpl(auth, firestore)
    }


    @Provides
    @ViewModelScoped
    fun providesGetListAddressUseCase(addressRemoteRepository: AddressRemoteRepository): GetListAddressUseCase {
        return GetListAddressUseCase(addressRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesGetAddressUseCase(addressRemoteRepository: AddressRemoteRepository): GetAddressUseCase {
        return GetAddressUseCase(addressRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesUpsertAddressUseCase(addressRemoteRepository: AddressRemoteRepository): UpsertAddressUseCase {
        return UpsertAddressUseCase(addressRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesDeleteAddressUseCase(addressRemoteRepository: AddressRemoteRepository): DeleteAddressUseCase {
        return DeleteAddressUseCase(addressRemoteRepository)
    }
    @Provides
    @ViewModelScoped
    fun providesGetFistAddressUseCase(addressRemoteRepository: AddressRemoteRepository): GetFistAddressUseCase {
        return GetFistAddressUseCase(addressRemoteRepository)
    }

}