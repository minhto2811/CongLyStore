package com.mgok.conglystore.data.remote.bill

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BillRemoteRepositoryImp @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : BillRemoteRepository {
    override suspend fun createBill(bill: Bill) {
        firestore.collection("bills")
            .document(bill.id)
            .set(bill.copy(userId = auth.currentUser!!.uid))
            .await()
    }

    override suspend fun getListBill(): List<Bill> {
        val list =
            firestore.collection("bills")
                .whereEqualTo("userId",auth.currentUser!!.uid)
                .whereNotEqualTo("delete", true)
                .get()
                .await()
        return list.toObjects(Bill::class.java)
    }

    override suspend fun getBillById(billId: String): Bill? {
        val list =
            firestore.collection("bills").document(billId).get().await()
        return list.toObject(Bill::class.java)
    }

    override suspend fun updatePaymentStatus(billId: String, status: Int) {
        firestore.collection("bills").document(billId).update("paymentStatus", status).await()
    }

    override suspend fun deleteBillById(billId: String) {
        firestore.collection("bills").document(billId).delete().await()
    }
}