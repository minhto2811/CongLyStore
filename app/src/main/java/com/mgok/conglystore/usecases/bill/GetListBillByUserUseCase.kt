package com.mgok.conglystore.usecases.bill

import com.mgok.conglystore.data.remote.bill.BillRemoteRepository
import javax.inject.Inject

class GetListBillByUserUseCase @Inject constructor(
    private val billRemoteRepository: BillRemoteRepository
) {
    suspend operator fun invoke() = billRemoteRepository.getListBillByUser()
}