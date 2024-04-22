package com.mgok.conglystore.data.remote.refund

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RefundRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : RefundRepository {
    override suspend fun createRefund(refund: Refund) {
        firestore.collection("refunds").document(refund.id)
            .set(refund).await()
    }

    override suspend fun getListRefund(status: Int): List<Refund> {
        val snap = firestore.collection("refunds").whereEqualTo("status", status).get().await()
        return snap.toObjects(Refund::class.java)
    }

    override suspend fun getListRefundByUser(status: Int): List<Refund> {
        val snap = firestore.collection("refunds")
            .whereEqualTo("userId", auth.currentUser!!.uid)
            .whereEqualTo("status", status)
            .get()
            .await()
        return snap.toObjects(Refund::class.java)
    }

    override suspend fun updateRefundStatus(refundId: String, status: Int) {
        firestore.collection("refunds").document(refundId).update("status", status).await()
    }
}