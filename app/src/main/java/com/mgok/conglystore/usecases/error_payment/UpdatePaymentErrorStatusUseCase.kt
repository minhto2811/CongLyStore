package com.mgok.conglystore.usecases.error_payment

import com.mgok.conglystore.data.remote.error_payment.ErrorPaymentRemote
import javax.inject.Inject

class UpdatePaymentErrorStatusUseCase @Inject constructor(
    private val errorPaymentRemote: ErrorPaymentRemote
) {
    suspend fun updateStatus(epId: String, status: Int) =
        errorPaymentRemote.updateStatus(epId, status)
}