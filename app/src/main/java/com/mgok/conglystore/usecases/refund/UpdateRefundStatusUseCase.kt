package com.mgok.conglystore.usecases.refund

import com.mgok.conglystore.data.remote.refund.RefundRepository
import javax.inject.Inject

class UpdateRefundStatusUseCase @Inject constructor(
    private val refundRepository: RefundRepository
) {
    suspend fun updateStatus(refundId: String, status: Int) =
        refundRepository.updateRefundStatus(refundId, status)
}