package com.mgok.conglystore.data.remote.cart

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CartRemoteRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : CartRemoteRepository {
    override suspend fun getListCart(): List<Cart> {
        val snapshot = firestore.collection("carts").whereEqualTo("userId", auth.uid).get().await()
        return snapshot.toObjects(Cart::class.java)
    }


    override suspend fun upsertCart(cart: Cart) {
        firestore.collection("carts").document(cart.id).set(cart.copy(userId = auth.uid.toString()))
            .await()
    }


    override suspend fun delete(cartId: String) {
        firestore.collection("carts").document(cartId).delete().await()
    }
}