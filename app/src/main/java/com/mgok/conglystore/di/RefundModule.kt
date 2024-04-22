package com.mgok.conglystore.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mgok.conglystore.data.remote.refund.RefundRepository
import com.mgok.conglystore.data.remote.refund.RefundRepositoryImpl
import com.mgok.conglystore.usecases.refund.CreateRefundUseCase
import com.mgok.conglystore.usecases.refund.GetListRefundByUserUseCase
import com.mgok.conglystore.usecases.refund.GetListRefundUseCase
import com.mgok.conglystore.usecases.refund.UpdateRefundStatusUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RefundModule {

    @Provides
    @Singleton
    fun providesRefundRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): RefundRepository {
        return RefundRepositoryImpl(firestore, auth)
    }

    @Provides
    @Singleton
    fun providesCreateRefundUseCase(refundRepository: RefundRepository): CreateRefundUseCase {
        return CreateRefundUseCase(refundRepository)
    }

    @Provides
    @Singleton
    fun providesGetListRefundUseCase(refundRepository: RefundRepository): GetListRefundUseCase {
        return GetListRefundUseCase(refundRepository)
    }

    @Provides
    @Singleton
    fun providesGetListRefundByUserUseCase(refundRepository: RefundRepository): GetListRefundByUserUseCase {
        return GetListRefundByUserUseCase(refundRepository)
    }

    @Provides
    @Singleton
    fun providesUpdateRefundStatusUseCase(refundRepository: RefundRepository): UpdateRefundStatusUseCase {
        return UpdateRefundStatusUseCase(refundRepository)
    }
}