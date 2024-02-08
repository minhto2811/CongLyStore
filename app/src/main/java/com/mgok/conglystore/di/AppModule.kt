package com.mgok.conglystore.di

import android.content.Context
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.mgok.conglystore.data.remote.UserRepository
import com.mgok.conglystore.presentation.auth.GoogleAuthUiClient
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
    fun providesUserRepository(auth: FirebaseAuth, firestore: FirebaseFirestore,googleAuthUiClient: GoogleAuthUiClient): UserRepository {
        return UserRepository(auth, firestore,googleAuthUiClient)
    }
}