package com.mgok.conglystore.data.remote.bill

interface BillRemoteRepository {
    suspend fun createBill(bill: Bill)
    suspend fun getListBill(): List<Bill>

    suspend fun getBillById(billId: String): Bill?

    suspend fun updatePaymentStatus(billId: String, status: Int)

    suspend fun deleteBillById(billId: String)
}