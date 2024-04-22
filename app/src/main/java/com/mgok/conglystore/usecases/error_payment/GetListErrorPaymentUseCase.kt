package com.mgok.conglystore.usecases.error_payment

import com.mgok.conglystore.data.remote.error_payment.ErrorPaymentRemote
import javax.inject.Inject

class GetListErrorPaymentUseCase @Inject constructor(
    private val errorPaymentRemote: ErrorPaymentRemote
) {
    suspend fun getAll(status:Int) = errorPaymentRemote.getAll(status)
}