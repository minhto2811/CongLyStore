package com.mgok.conglystore.data.remote.refund

interface RefundRepository {
   suspend fun createRefund(refund: Refund)

    suspend fun getListRefund(status:Int): List<Refund>

    suspend  fun getListRefundByUser(status: Int): List<Refund>

    suspend fun updateRefundStatus(refundId: String, status: Int)
}