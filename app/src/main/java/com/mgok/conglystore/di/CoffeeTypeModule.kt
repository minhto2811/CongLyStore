package com.mgok.conglystore.di

import com.google.firebase.firestore.FirebaseFirestore
import com.mgok.conglystore.data.remote.coffee_type.CoffeeTypeRemoteRepository
import com.mgok.conglystore.data.remote.coffee_type.CoffeeTypeRemoteRepositoryImpl
import com.mgok.conglystore.usecases.coffee_type.DeleteCoffeeTypeUseCase
import com.mgok.conglystore.usecases.coffee_type.InsertCoffeeTypeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object CoffeeTypeModule {
    @Provides
    @ViewModelScoped
    fun providesCoffeeTypeRemoteRepository(firestore: FirebaseFirestore): CoffeeTypeRemoteRepository {
        return CoffeeTypeRemoteRepositoryImpl(firestore)
    }

    @Provides
    @ViewModelScoped
    fun providesInsertCoffeeTypeUseCase(coffeeTypeRemoteRepository: CoffeeTypeRemoteRepository): InsertCoffeeTypeUseCase {
        return InsertCoffeeTypeUseCase(coffeeTypeRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesDeleteCoffeeTypeUseCase(coffeeTypeRemoteRepository: CoffeeTypeRemoteRepository): DeleteCoffeeTypeUseCase {
        return DeleteCoffeeTypeUseCase(coffeeTypeRemoteRepository)
    }
}