package com.mgok.conglystore.data.remote.coffee

import android.net.Uri
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CoffeeRemoteRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : CoffeeRemoteRepository {


    override suspend fun uploadImage(uri: Uri, uid: String): Uri {
        val avatarRef = storage.reference.child("products/$uid.png")
        val uploadTask = avatarRef.putFile(uri).await()
        return uploadTask.storage.downloadUrl.await()
    }

    override suspend fun deleteImage(uri: String) {
        storage.getReferenceFromUrl(uri).delete().await()
    }

    override suspend fun updateSold(idCoffee: String, sold: Int) {
        val snapshot = firestore.collection("coffees").document(idCoffee)
        val coffee = snapshot.get().await()
        if (coffee.exists()) {
            val data = coffee.toObject(Coffee::class.java)!!.sold + sold
            snapshot.update("quantity", data).await()
        }
    }

    override suspend fun getListCoffeeBySold(): List<Coffee> {
        val snapshot =
            firestore.collection("coffees").whereEqualTo("delete", false).orderBy("quantity")
                .orderBy("sold").get().await()
        return snapshot.toObjects(Coffee::class.java)
    }

    override suspend fun filterCoffeeByQuery(querry: String): List<Coffee> {
        val snapshot = firestore.collection("coffees")
            .where(
                Filter.and(
                    Filter.or(
                        Filter.greaterThanOrEqualTo("name", querry),
                        Filter.greaterThanOrEqualTo("type", querry),
                    ),
                    Filter.equalTo("delete", false)
                )
            )
            .get().await()
        return snapshot.toObjects(Coffee::class.java)
    }

    override suspend fun deleteCoffee(idCoffee: String) {
        firestore.collection("coffees").document(idCoffee).update("delete", true).await()
    }

    override suspend fun getListCoffee(): List<Coffee> {
        val snapshot = firestore.collection("coffees").whereEqualTo("delete", false).get().await()
        return snapshot.toObjects(Coffee::class.java)
    }

    override suspend fun getById(idCoffee: String): Coffee? {
        val snapshot = firestore.collection("coffees").document(idCoffee).get().await()
        return snapshot.toObject(Coffee::class.java)
    }


    override suspend fun insertCoffee(coffee: Coffee) {
        firestore.collection("coffees").document(coffee.id).set(coffee).await()
    }

    override suspend fun getListCoffeeByName(coffeeName: String): List<Coffee> {
        val snapshot =
            firestore.collection("coffees").whereEqualTo("name", coffeeName)
                .whereEqualTo("delete", false).orderBy("sold").get().await()
        return snapshot.toObjects(Coffee::class.java)
    }


}