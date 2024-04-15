package com.mgok.conglystore.usecases.bill

import com.mgok.conglystore.data.remote.bill.BillRemoteRepository
import javax.inject.Inject

class UpdatePaymentStatusUseCase @Inject constructor(
    private val billRemoteRepository: BillRemoteRepository
) {
    suspend fun updateStatus(billId: String, status: Int) =
        billRemoteRepository.updatePaymentStatus(billId, status)
}