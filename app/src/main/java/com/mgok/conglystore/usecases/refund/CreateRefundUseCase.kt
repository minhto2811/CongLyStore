package com.mgok.conglystore.usecases.refund

import com.mgok.conglystore.data.remote.refund.Refund
import com.mgok.conglystore.data.remote.refund.RefundRepository
import javax.inject.Inject

class CreateRefundUseCase @Inject constructor(
    private val refundRepository: RefundRepository
) {
    suspend fun createRefund(refund: Refund) = refundRepository.createRefund(refund)
}