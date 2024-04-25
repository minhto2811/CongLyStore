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
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UserModule {
    @Provides
    @ViewModelScoped
    fun providesUserRemoteRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        googleAuthUiClient: GoogleAuthUiClient,
        storage: FirebaseStorage
    ): UserRemoteRepository {
        return UserRemoteRepositoryImpl(auth, firestore, googleAuthUiClient, storage)
    }


    @Provides
    @ViewModelScoped
    fun providesGetInfoUserUseCase(userRemoteRepository: UserRemoteRepository): GetInfoUserUseCase {
        return GetInfoUserUseCase(userRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesUserSignOutUseCase(userRemoteRepository: UserRemoteRepository): UserSignOutUseCase {
        return UserSignOutUseCase(userRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesGetFireBaseUserUseCase(auth: FirebaseAuth): GetFireBaseUserUseCase {
        return GetFireBaseUserUseCase(auth)
    }

    @Provides
    @ViewModelScoped
    fun providesSignInResultUseCase(userRemoteRepository: UserRemoteRepository): SignInResultUseCase {
        return SignInResultUseCase(userRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesLoginWithAccountUseCase(userRemoteRepository: UserRemoteRepository): LoginWithAccountUseCase {
        return LoginWithAccountUseCase(userRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesGoogleAuthUiClientSignInUseCase(userRemoteRepository: UserRemoteRepository): GoogleAuthUiClientSignInUseCase {
        return GoogleAuthUiClientSignInUseCase(userRemoteRepository)
    }



    @Provides
    @ViewModelScoped
    fun providesHandleFacebookAccessTokenUseCase(userRemoteRepository: UserRemoteRepository): HandleFacebookAccessTokenUseCase {
        return HandleFacebookAccessTokenUseCase(userRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesCreateAccountUseCase(userRemoteRepository: UserRemoteRepository): CreateAccountUseCase {
        return CreateAccountUseCase(userRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesResetPasswordUseCase(userRemoteRepository: UserRemoteRepository): ResetPasswordUseCase {
        return ResetPasswordUseCase(userRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesUpdateAvatarWithLinkUserCase(userRemoteRepository: UserRemoteRepository): UpdateAvatarWithLinkUserCase {
        return UpdateAvatarWithLinkUserCase(userRemoteRepository)
    }


    @Provides
    @ViewModelScoped
    fun providesUpdateAvatar(userRemoteRepository: UserRemoteRepository): UpdateAvatarUseCase {
        return UpdateAvatarUseCase(userRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesDeleteAvatarUseCase(userRemoteRepository: UserRemoteRepository): DeleteAvatarUseCase {
        return DeleteAvatarUseCase(userRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesUpdateInfoUserUseCase(userRemoteRepository: UserRemoteRepository): UpdateInfoUserUseCase {
        return UpdateInfoUserUseCase(userRemoteRepository)
    }



}