package com.mgok.conglystore.usecases.bill

import com.mgok.conglystore.data.remote.bill.BillRemoteRepository
import javax.inject.Inject

class GetBillByIdUseCase @Inject constructor(
    private val billRemoteRepository: BillRemoteRepository
) {
    suspend fun getBillById(billId: String) = billRemoteRepository.getBillById(billId)
}