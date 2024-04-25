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
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object BillModule {

    @Provides
    @ViewModelScoped
    fun providesBillRemoteREpository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): BillRemoteRepository {
        return BillRemoteRepositoryImp(firestore, auth)
    }

    @Provides
    @ViewModelScoped
    fun providesGetListBillByUserUseCase(billRemoteRepository: BillRemoteRepository): GetListBillByUserUseCase {
        return GetListBillByUserUseCase(billRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesGetListBillUseCase(billRemoteRepository: BillRemoteRepository): GetListBillUseCase {
        return GetListBillUseCase(billRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesGetBillByIdUseCase(billRemoteRepository: BillRemoteRepository): GetBillByIdUseCase {
        return GetBillByIdUseCase(billRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesUpdatePaymentStatusUseCase(billRemoteRepository: BillRemoteRepository): UpdatePaymentStatusUseCase {
        return UpdatePaymentStatusUseCase(billRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesUpdateStatusBillUseCase(billRemoteRepository: BillRemoteRepository): UpdateStatusBillUseCase {
        return UpdateStatusBillUseCase(billRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesDeleteBillUseCase(billRemoteRepository: BillRemoteRepository): DeleteBillUseCase {
        return DeleteBillUseCase(billRemoteRepository)
    }

    @Provides
    @ViewModelScoped
    fun providesCreateBillUseCase(billRemoteRepository: BillRemoteRepository): CreateBillUseCase {
        return CreateBillUseCase(billRemoteRepository)
    }
    @Provides
    @ViewModelScoped
    fun providesGetListBillByDateUseCase(billRemoteRepository: BillRemoteRepository): GetListBillByDateUseCase {
        return GetListBillByDateUseCase(billRemoteRepository)
    }

}