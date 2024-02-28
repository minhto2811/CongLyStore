package com.mgok.conglystore.data.remote.coffee_type

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CoffeeTypeRemoteRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : CoffeeTypeRemoteRepository {
    override suspend fun insertCoffeType(coffeeType: CoffeeType) {
        firestore.collection("coffee_types").document(coffeeType.id).set(coffeeType)
            .await()
    }

    override suspend fun getListCoffeeType(): List<CoffeeType> {
        val snapshot = firestore.collection("coffee_types").get().await()
        return snapshot.toObjects(CoffeeType::class.java)
    }

    override suspend fun deleteCoffeeType(id: String) {
        firestore.collection("coffee_types").document(id).delete().await()
    }
}