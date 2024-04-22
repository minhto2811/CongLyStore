package com.mgok.conglystore.usecases.error_payment

import com.mgok.conglystore.data.remote.error_payment.ErrorPaymentRemote
import javax.inject.Inject

class GetListErrorPaymentByUserUseCase @Inject constructor(
    private val errorPaymentRemote: ErrorPaymentRemote
) {
    suspend fun selectByStatus(status:Int) = errorPaymentRemote.getByUser(status)
}