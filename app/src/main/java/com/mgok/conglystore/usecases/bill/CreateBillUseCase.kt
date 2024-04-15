package com.mgok.conglystore.usecases.bill

import com.mgok.conglystore.data.remote.bill.Bill
import com.mgok.conglystore.data.remote.bill.BillRemoteRepository
import javax.inject.Inject

class CreateBillUseCase @Inject constructor(
    private val billRemoteRepository: BillRemoteRepository
) {
    suspend fun createBill(bill: Bill) = billRemoteRepository.createBill(bill)
}