package com.mgok.conglystore.di

import android.content.Context
import androidx.room.Room
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.mgok.conglystore.data.local.MyDatabase
import com.mgok.conglystore.data.local.coffee.CoffeeLocalRepository
import com.mgok.conglystore.data.local.coffee.CoffeeLocalRepositoryImpl
import com.mgok.conglystore.data.remote.coffee.CoffeeRemoteRepository
import com.mgok.conglystore.data.remote.coffee.CoffeeRemoteRepositoryImpl
import com.mgok.conglystore.data.remote.user.UserRemoteRepository
import com.mgok.conglystore.data.remote.user.UserRemoteRepositoryImpl
import com.mgok.conglystore.presentation.auth.sign_in.GoogleAuthUiClient
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
    fun providesFirebaseAuth() = Firebase.auth

    @Provides
    @Singleton
    fun providesFirebaseFirestore() = Firebase.firestore

    @Provides
    @Singleton
    fun providesFirebaseStorage() = Firebase.storage

    @Provides
    @Singleton
    fun providesSignInClient(@ApplicationContext context: Context): SignInClient {
        return Identity.getSignInClient(context)
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
    fun providesRoomDatabase(@ApplicationContext context: Context): MyDatabase {
        return Room.databaseBuilder(
            context,
            MyDatabase::class.java,
            "my_db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesUserRemoteRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        googleAuthUiClient: GoogleAuthUiClient,
        storage: FirebaseStorage
    ): UserRemoteRepository {
        return UserRemoteRepositoryImpl(auth, firestore, googleAuthUiClient, storage)
    }

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
    fun providesCoffeeLocalRepository(
        database: MyDatabase
    ): CoffeeLocalRepository {
        return CoffeeLocalRepositoryImpl(database)
    }

    @Provides
    @Singleton
    fun providesFusedLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }


}