package com.mgok.conglystore.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mgok.conglystore.data.remote.bill.BillRemoteRepository
import com.mgok.conglystore.data.remote.bill.BillRemoteRepositoryImp
import com.mgok.conglystore.usecases.bill.CreateBillUseCase
import com.mgok.conglystore.usecases.bill.DeleteBillUseCase
import com.mgok.conglystore.usecases.bill.GetBillByIdUseCase
import com.mgok.conglystore.usecases.bill.GetListBillByDateUseCase
import com.mgok.conglystore.usecases.bill.GetListBillByUserUseCase
import com.mgok.conglystore.usecases.bill.GetListBillUseCase
import com.mgok.conglystore.usecases.bill.UpdatePaymentStatusUseCase
import com.mgok.conglystore.usecases.bill.UpdateStatusBillUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BillModule {

    @Provides
    @Singleton
    fun providesBillRemoteREpository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): BillRemoteRepository {
        return BillRemoteRepositoryImp(firestore, auth)
    }

    @Provides
    @Singleton
    fun providesGetListBillByUserUseCase(billRemoteRepository: BillRemoteRepository): GetListBillByUserUseCase {
        return GetListBillByUserUseCase(billRemoteRepository)
    }

    @Provides
    @Singleton
    fun providesGetListBillUseCase(billRemoteRepository: BillRemoteRepository): GetListBillUseCase {
        return GetListBillUseCase(billRemoteRepository)
    }

    @Provides
    @Singleton
    fun providesGetBillByIdUseCase(billRemoteRepository: BillRemoteRepository): GetBillByIdUseCase {
        return GetBillByIdUseCase(billRemoteRepository)
    }

    @Provides
    @Singleton
    fun providesUpdatePaymentStatusUseCase(billRemoteRepository: BillRemoteRepository): UpdatePaymentStatusUseCase {
        return UpdatePaymentStatusUseCase(billRemoteRepository)
    }

    @Provides
    @Singleton
    fun providesUpdateStatusBillUseCase(billRemoteRepository: BillRemoteRepository): UpdateStatusBillUseCase {
        return UpdateStatusBillUseCase(billRemoteRepository)
    }

    @Provides
    @Singleton
    fun providesDeleteBillUseCase(billRemoteRepository: BillRemoteRepository): DeleteBillUseCase {
        return DeleteBillUseCase(billRemoteRepository)
    }

    @Provides
    @Singleton
    fun providesCreateBillUseCase(billRemoteRepository: BillRemoteRepository): CreateBillUseCase {
        return CreateBillUseCase(billRemoteRepository)
    }
    @Provides
    @Singleton
    fun providesGetListBillByDateUseCase(billRemoteRepository: BillRemoteRepository): GetListBillByDateUseCase {
        return GetListBillByDateUseCase(billRemoteRepository)
    }

}