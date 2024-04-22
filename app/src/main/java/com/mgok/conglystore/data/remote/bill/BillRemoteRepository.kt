package com.mgok.conglystore.data.remote.bill

interface BillRemoteRepository {
    suspend fun createBill(bill: Bill)
    suspend fun getListBill(status: Int): List<Bill>
    suspend fun getListBillByUser(): List<Bill>

    suspend fun getBillById(billId: String): Bill?

    suspend fun updatePaymentStatus(billId: String, status: Int)

    suspend fun updateStatus(billId: String, status: Int)

    suspend fun deleteBillById(billId: String)

    suspend fun filterBillByDate(dateStart: Long, dateEnd: Long): List<Bill>
}