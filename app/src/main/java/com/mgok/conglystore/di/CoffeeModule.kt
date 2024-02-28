package com.mgok.conglystore.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mgok.conglystore.data.remote.coffee.CoffeeRemoteRepository
import com.mgok.conglystore.data.remote.coffee.CoffeeRemoteRepositoryImpl
import com.mgok.conglystore.usecases.coffee.GetCoffeeByIdUseCase
import com.mgok.conglystore.usecases.coffee.GetListCoffeeByNameUseCase
import com.mgok.conglystore.usecases.coffee.GetListCoffeeUseCase
import com.mgok.conglystore.usecases.coffee.InsertCoffeeUseCase
import com.mgok.conglystore.usecases.image.DeleteImageUseCase
import com.mgok.conglystore.usecases.image.UploadImageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoffeeModule {
    @Provides
    @Singleton
    fun providesCoffeeRemoteRepository(
        firestore: FirebaseFirestore,
        storage: FirebaseStorage
    ): CoffeeRemoteRepository {
        return CoffeeRemoteRepositoryImpl(firestore, storage)
    }


    @Provides
    @Singleton
    fun providesGetCoffeeByIdUseCase(coffeeRemoteRepository: CoffeeRemoteRepository): GetCoffeeByIdUseCase {
        return GetCoffeeByIdUseCase(coffeeRemoteRepository)
    }

    @Provides
    @Singleton
    fun providesGetListCoffeeByName(coffeeRemoteRepository: CoffeeRemoteRepository): GetListCoffeeByNameUseCase {
        return GetListCoffeeByNameUseCase(coffeeRemoteRepository)
    }

    @Provides
    @Singleton
    fun providesGetListCoffeeUseCase(coffeeRemoteRepository: CoffeeRemoteRepository): GetListCoffeeUseCase {
        return GetListCoffeeUseCase(coffeeRemoteRepository)
    }

    @Provides
    @Singleton
    fun providesUploadImageUseCase(coffeeRemoteRepository: CoffeeRemoteRepository): UploadImageUseCase {
        return UploadImageUseCase(coffeeRemoteRepository)
    }

    @Provides
    @Singleton
    fun providesInsertCoffeeUseCase(coffeeRemoteRepository: CoffeeRemoteRepository): InsertCoffeeUseCase {
        return InsertCoffeeUseCase(coffeeRemoteRepository)
    }

    @Provides
    @Singleton
    fun providesDeleteImageUseCase(coffeeRemoteRepository: CoffeeRemoteRepository): DeleteImageUseCase {
        return DeleteImageUseCase(coffeeRemoteRepository)
    }
}