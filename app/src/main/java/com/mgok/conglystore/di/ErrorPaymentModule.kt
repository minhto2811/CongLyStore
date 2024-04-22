package com.mgok.conglystore.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mgok.conglystore.data.remote.error_payment.ErrorPaymentRemote
import com.mgok.conglystore.data.remote.error_payment.ErrorPaymentRemoteImpl
import com.mgok.conglystore.usecases.error_payment.CreateErrorPaymentUseCase
import com.mgok.conglystore.usecases.error_payment.GetListErrorPaymentByUserUseCase
import com.mgok.conglystore.usecases.error_payment.GetListErrorPaymentUseCase
import com.mgok.conglystore.usecases.error_payment.UpdatePaymentErrorStatusUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ErrorPaymentModule {

    @Provides
    @Singleton
    fun providesErrorPaymentRemote(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): ErrorPaymentRemote {
        return ErrorPaymentRemoteImpl(firestore, auth)
    }

    @Provides
    @Singleton
    fun providesGetListErrorPaymentUseCase(errorPaymentRemote: ErrorPaymentRemote): GetListErrorPaymentUseCase {
        return GetListErrorPaymentUseCase(errorPaymentRemote)
    }

    @Provides
    @Singleton
    fun providesGetListErrorPaymentByUserUseCase(errorPaymentRemote: ErrorPaymentRemote): GetListErrorPaymentByUserUseCase {
        return GetListErrorPaymentByUserUseCase(errorPaymentRemote)
    }

    @Provides
    @Singleton
    fun providesCreateErrorPaymentUseCase(errorPaymentRemote: ErrorPaymentRemote): CreateErrorPaymentUseCase {
        return CreateErrorPaymentUseCase(errorPaymentRemote)
    }

    @Provides
    @Singleton
    fun providesUpdatePaymentErrorStatus(errorPaymentRemote: ErrorPaymentRemote): UpdatePaymentErrorStatusUseCase {
        return UpdatePaymentErrorStatusUseCase(errorPaymentRemote)
    }

}