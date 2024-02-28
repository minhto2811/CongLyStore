package com.mgok.conglystore.di

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mgok.conglystore.presentation.auth.sign_in.GoogleAuthUiClient
import com.mgok.conglystore.usecases.map.GetListLocationByNameUseCase
import com.mgok.conglystore.usecases.map.GetLocationByLatlngUseCase
import com.mgok.conglystore.usecases.map.GetLocationCurrentUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesGeoCoder(@ApplicationContext context: Context): Geocoder {
        return Geocoder(context)
    }

    @Provides
    @Singleton
    fun providesGoogleAuthUiClient(
        @ApplicationContext context: Context,
        signInClient: SignInClient
    ): GoogleAuthUiClient {
        return GoogleAuthUiClient(context, signInClient)
    }

    @Provides
    @Singleton
    fun providesFusedLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    @Singleton
    fun providesSignInClient(@ApplicationContext context: Context): SignInClient {
        return Identity.getSignInClient(context)
    }

    @Provides
    @Singleton
    fun providesGetLocationCurrentUseCase(fusedLocationProviderClient: FusedLocationProviderClient): GetLocationCurrentUseCase {
        return GetLocationCurrentUseCase(fusedLocationProviderClient)
    }

    @Provides
    @Singleton
    fun providesGetLocationByLatlngUseCase(geocoder: Geocoder): GetLocationByLatlngUseCase {
        return GetLocationByLatlngUseCase(geocoder)
    }

    @Provides
    @Singleton
    fun providesGetListLocationByNameUseCase(geocoder: Geocoder): GetListLocationByNameUseCase {
        return GetListLocationByNameUseCase(geocoder)
    }

}