package com.mgok.conglystore.data.remote.error_payment

interface ErrorPaymentRemote {
    suspend fun getAll(status: Int): List<ErrorPayment>

    suspend fun getByUser(status: Int): List<ErrorPayment>
    suspend fun createResponse(data: ErrorPayment)

    suspend fun updateStatus(epId: String, status: Int)

}