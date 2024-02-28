package com.mgok.conglystore.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mgok.conglystore.data.remote.user.UserRemoteRepository
import com.mgok.conglystore.data.remote.user.UserRemoteRepositoryImpl
import com.mgok.conglystore.presentation.auth.sign_in.GoogleAuthUiClient
import com.mgok.conglystore.usecases.user.CreateAccountUseCase
import com.mgok.conglystore.usecases.user.DeleteAvatarUseCase
import com.mgok.conglystore.usecases.user.GetFireBaseUserUseCase
import com.mgok.conglystore.usecases.user.GetInfoUserUseCase
import com.mgok.conglystore.usecases.user.GoogleAuthUiClientSignInUseCase
import com.mgok.conglystore.usecases.user.HandleFacebookAccessTokenUseCase
import com.mgok.conglystore.usecases.user.LoginWithAccountUseCase
import com.mgok.conglystore.usecases.user.ResetPasswordUseCase
import com.mgok.conglystore.usecases.user.SignInResultUseCase
import com.mgok.conglystore.usecases.user.UpdateAvatarUseCase
import com.mgok.conglystore.usecases.user.UpdateAvatarWithLinkUserCase
import com.mgok.conglystore.usecases.user.UpdateInfoUserUseCase
import com.mgok.conglystore.usecases.user.UserSignOutUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {
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
    fun providesGetInfoUserUseCase(userRemoteRepository: UserRemoteRepository): GetInfoUserUseCase {
        return GetInfoUserUseCase(userRemoteRepository)
    }

    @Provides
    @Singleton
    fun providesUserSignOutUseCase(userRemoteRepository: UserRemoteRepository): UserSignOutUseCase {
        return UserSignOutUseCase(userRemoteRepository)
    }

    @Provides
    @Singleton
    fun providesGetFireBaseUserUseCase(auth: FirebaseAuth): GetFireBaseUserUseCase {
        return GetFireBaseUserUseCase(auth)
    }

    @Provides
    @Singleton
    fun providesSignInResultUseCase(userRemoteRepository: UserRemoteRepository): SignInResultUseCase {
        return SignInResultUseCase(userRemoteRepository)
    }

    @Provides
    @Singleton
    fun providesLoginWithAccountUseCase(userRemoteRepository: UserRemoteRepository): LoginWithAccountUseCase {
        return LoginWithAccountUseCase(userRemoteRepository)
    }

    @Provides
    @Singleton
    fun providesGoogleAuthUiClientSignInUseCase(userRemoteRepository: UserRemoteRepository): GoogleAuthUiClientSignInUseCase {
        return GoogleAuthUiClientSignInUseCase(userRemoteRepository)
    }



    @Provides
    @Singleton
    fun providesHandleFacebookAccessTokenUseCase(userRemoteRepository: UserRemoteRepository): HandleFacebookAccessTokenUseCase {
        return HandleFacebookAccessTokenUseCase(userRemoteRepository)
    }

    @Provides
    @Singleton
    fun providesCreateAccountUseCase(userRemoteRepository: UserRemoteRepository): CreateAccountUseCase {
        return CreateAccountUseCase(userRemoteRepository)
    }

    @Provides
    @Singleton
    fun providesResetPasswordUseCase(userRemoteRepository: UserRemoteRepository): ResetPasswordUseCase {
        return ResetPasswordUseCase(userRemoteRepository)
    }

    @Provides
    @Singleton
    fun providesUpdateAvatarWithLinkUserCase(userRemoteRepository: UserRemoteRepository): UpdateAvatarWithLinkUserCase {
        return UpdateAvatarWithLinkUserCase(userRemoteRepository)
    }


    @Provides
    @Singleton
    fun providesUpdateAvatar(userRemoteRepository: UserRemoteRepository): UpdateAvatarUseCase {
        return UpdateAvatarUseCase(userRemoteRepository)
    }

    @Provides
    @Singleton
    fun providesDeleteAvatarUseCase(userRemoteRepository: UserRemoteRepository): DeleteAvatarUseCase {
        return DeleteAvatarUseCase(userRemoteRepository)
    }

    @Provides
    @Singleton
    fun providesUpdateInfoUserUseCase(userRemoteRepository: UserRemoteRepository): UpdateInfoUserUseCase {
        return UpdateInfoUserUseCase(userRemoteRepository)
    }



}