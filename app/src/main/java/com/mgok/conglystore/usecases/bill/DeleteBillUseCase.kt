package com.mgok.conglystore.usecases.bill

import com.mgok.conglystore.data.remote.bill.BillRemoteRepository
import javax.inject.Inject

class DeleteBillUseCase @Inject constructor(
    private val billRemoteRepository: BillRemoteRepository
) {
    suspend fun deleteBillById(billId: String) = billRemoteRepository.deleteBillById(billId)
}