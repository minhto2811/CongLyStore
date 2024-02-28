package com.mgok.conglystore.data.remote.favorite

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FavoriteRemoteRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : FavoriteRemoteRepository {
    override suspend fun getListFavourite(): List<String>? {
        val snapshot = firestore.collection("favorites").document(auth.uid.toString()).get().await()
        return snapshot.toObject(Favorite::class.java)?.favorites
    }


    override fun addItemFavorite(coffeeId: String) {
        val map = mapOf(
            "favorites" to FieldValue.arrayUnion(coffeeId)
        )
        firestore.collection("favorites").document(auth.uid.toString())
            .set(map, SetOptions.merge())
    }

    override fun removeItemFavorite(coffeeId: String) {
        val map = mapOf(
            "favorites" to FieldValue.arrayRemove(coffeeId)
        )
        firestore.collection("favorites").document(auth.uid.toString())
            .set(map, SetOptions.merge())
    }
}