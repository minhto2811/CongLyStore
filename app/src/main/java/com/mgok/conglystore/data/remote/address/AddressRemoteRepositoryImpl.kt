package com.mgok.conglystore.data.remote.address

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AddressRemoteRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AddressRemoteRepository {
    override suspend fun getListAddress(): List<Address> {
        val snapshot =
            firestore.collection("address").whereEqualTo("userId", auth.uid).get().await()
        return snapshot.toObjects(Address::class.java)
    }

    override suspend fun getAddress(addressId: String): Address? {
        val snapshot = firestore.collection("address").document(addressId).get().await()
        return snapshot.toObject(Address::class.java)
    }

    override suspend fun upsertAddress(address: Address) {
        firestore.collection("address").document(address.id)
            .set(address.copy(userId = auth.uid.toString()))
            .await()
    }

    override suspend fun deleteAddress(addressId: String) {
        firestore.collection("address").document(addressId).delete().await()
    }
}