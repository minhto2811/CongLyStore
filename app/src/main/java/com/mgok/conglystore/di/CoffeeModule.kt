package com.mgok.conglystore.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mgok.conglystore.data.remote.coffee.CoffeeRemoteRepository
import com.mgok.conglystore.data.remote.coffee.CoffeeRemoteRepositoryImpl
import com.mgok.conglystore.usecases.coffee.DeleteCoffeeByIdUseCase
import com.mgok.conglystore.usecases.coffee.DeleteCoffeeByTypeUseCase
import com.mgok.conglystore.usecases.coffee.FilterCoffeebyQuerryUseCase
import com.mgok.conglystore.usecases.coffee.GetCoffeeByIdUseCase
import com.mgok.conglystore.usecases.coffee.GetListCoffeeByNameUseCase
import com.mgok.conglystore.usecases.coffee.GetListCoffeeBySold
import com.mgok.conglystore.usecases.coffee.GetListCoffeeUseCase
import com.mgok.conglystore.usecases.coffee.InsertCoffeeUseCase
import com.mgok.conglystore.usecases.coffee.UpdateSoldProductUseCase
import com.mgok.conglystore.usecases.image.DeleteImageUseCase
import com.mgok.conglystore.usecases.image.UploadImageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object CoffeeModule {
    @Provides
    @ViewModelScoped
    fun providesCoffeeRemoteRepository(
        firestore: FirebaseFirestore,
        storage: FirebaseStorage
    ): CoffeeRemoteRepository {
        return CoffeeRemoteRepositoryImpl(firestore, storage)
    }


    @Provides
    @ViewModelScoped
    fun providesGetCoffeeByIdUseCase(coffeeRemoteRepository: CoffeeRemoteRepository): GetCoffeeByIdUseCase {
        return GetCoffeeByIdUseCase(coffeeRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesGetListCoffeeByName(coffeeRemoteRepository: CoffeeRemoteRepository): GetListCoffeeByNameUseCase {
        return GetListCoffeeByNameUseCase(coffeeRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesGetListCoffeeUseCase(coffeeRemoteRepository: CoffeeRemoteRepository): GetListCoffeeUseCase {
        return GetListCoffeeUseCase(coffeeRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesUploadImageUseCase(coffeeRemoteRepository: CoffeeRemoteRepository): UploadImageUseCase {
        return UploadImageUseCase(coffeeRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesInsertCoffeeUseCase(coffeeRemoteRepository: CoffeeRemoteRepository): InsertCoffeeUseCase {
        return InsertCoffeeUseCase(coffeeRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesDeleteImageUseCase(coffeeRemoteRepository: CoffeeRemoteRepository): DeleteImageUseCase {
        return DeleteImageUseCase(coffeeRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesUpdateSoldProductUseCase(coffeeRemoteRepository: CoffeeRemoteRepository): UpdateSoldProductUseCase {
        return UpdateSoldProductUseCase(coffeeRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesGetListCoffeeBySold(coffeeRemoteRepository: CoffeeRemoteRepository): GetListCoffeeBySold {
        return GetListCoffeeBySold(coffeeRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesFilterCoffeebyQuerryUseCase(coffeeRemoteRepository: CoffeeRemoteRepository): FilterCoffeebyQuerryUseCase {
        return FilterCoffeebyQuerryUseCase(coffeeRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesDeleteCoffeeByIdUseCase(coffeeRemoteRepository: CoffeeRemoteRepository): DeleteCoffeeByIdUseCase {
        return DeleteCoffeeByIdUseCase(coffeeRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesDeleteCoffeeTypeUseCase(coffeeRemoteRepository: CoffeeRemoteRepository): DeleteCoffeeByTypeUseCase {
        return DeleteCoffeeByTypeUseCase(coffeeRemoteRepository)
    }

}