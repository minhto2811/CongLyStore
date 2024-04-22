package com.mgok.conglystore.usecases.bill

import com.mgok.conglystore.data.remote.bill.BillRemoteRepository
import javax.inject.Inject

class GetListBillUseCase @Inject constructor(
    private val billRemoteRepository: BillRemoteRepository
) {

    suspend operator fun invoke(status:Int) = billRemoteRepository.getListBill(status)
}