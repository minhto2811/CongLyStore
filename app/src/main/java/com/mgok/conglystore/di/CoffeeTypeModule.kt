package com.mgok.conglystore.di

import com.google.firebase.firestore.FirebaseFirestore
import com.mgok.conglystore.data.remote.coffee_type.CoffeeTypeRemoteRepository
import com.mgok.conglystore.data.remote.coffee_type.CoffeeTypeRemoteRepositoryImpl
import com.mgok.conglystore.usecases.coffee_type.DeleteCoffeeTypeUseCase
import com.mgok.conglystore.usecases.coffee_type.InsertCoffeeTypeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoffeeTypeModule {
    @Provides
    @Singleton
    fun providesCoffeeTypeRemoteRepository(firestore: FirebaseFirestore): CoffeeTypeRemoteRepository {
        return CoffeeTypeRemoteRepositoryImpl(firestore)
    }

    @Provides
    @Singleton
    fun providesInsertCoffeeTypeUseCase(coffeeTypeRemoteRepository: CoffeeTypeRemoteRepository): InsertCoffeeTypeUseCase {
        return InsertCoffeeTypeUseCase(coffeeTypeRemoteRepository)
    }

    @Provides
    @Singleton
    fun providesDeleteCoffeeTypeUseCase(coffeeTypeRemoteRepository: CoffeeTypeRemoteRepository): DeleteCoffeeTypeUseCase {
        return DeleteCoffeeTypeUseCase(coffeeTypeRemoteRepository)
    }
}