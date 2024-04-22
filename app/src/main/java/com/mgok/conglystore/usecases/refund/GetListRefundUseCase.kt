package com.mgok.conglystore.usecases.refund

import com.mgok.conglystore.data.remote.refund.RefundRepository
import javax.inject.Inject

class GetListRefundUseCase @Inject constructor(
    private val refundRepository: RefundRepository
) {
    suspend operator fun invoke(status:Int) = refundRepository.getListRefund(status)
}