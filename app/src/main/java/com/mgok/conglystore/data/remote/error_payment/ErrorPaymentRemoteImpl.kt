package com.mgok.conglystore.data.remote.error_payment

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ErrorPaymentRemoteImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ErrorPaymentRemote {
    override suspend fun getAll(status: Int): List<ErrorPayment> {
        val snapshot =
            firestore.collection("error_payments").whereEqualTo("status", status).get().await()
        return snapshot.toObjects(ErrorPayment::class.java)
    }

    override suspend fun getByUser(status: Int): List<ErrorPayment> {
        val snapshot =
            firestore.collection("error_payments")
                .whereEqualTo("userId", auth.currentUser!!.uid)
                .whereEqualTo("status", status)
                .get()
                .await()
        return snapshot.toObjects(ErrorPayment::class.java)
    }

    override suspend fun createResponse(data: ErrorPayment) {
        firestore.collection("error_payments").document(data.id)
            .set(data.copy(userId = auth.currentUser!!.uid)).await()
    }

    override suspend fun updateStatus(epId: String, status: Int) {
        firestore.collection("error_payments")
            .document(epId)
            .update("status", status)
            .await()
    }

}