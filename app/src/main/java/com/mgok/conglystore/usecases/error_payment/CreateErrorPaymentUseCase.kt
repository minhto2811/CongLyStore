package com.mgok.conglystore.usecases.error_payment

import com.mgok.conglystore.data.remote.error_payment.ErrorPayment
import com.mgok.conglystore.data.remote.error_payment.ErrorPaymentRemote
import javax.inject.Inject

class CreateErrorPaymentUseCase @Inject constructor(
    private val errorPaymentRemote: ErrorPaymentRemote
) {
    suspend fun create(errorPayment: ErrorPayment) = errorPaymentRemote.createResponse(errorPayment)
}