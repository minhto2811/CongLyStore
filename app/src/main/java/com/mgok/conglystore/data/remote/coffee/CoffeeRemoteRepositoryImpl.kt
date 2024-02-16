package com.mgok.conglystore.data.remote.coffee

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mgok.conglystore.data.local.coffee.CoffeeType
import com.mgok.conglystore.presentation.auth.ResultStatusState
import com.mgok.conglystore.presentation.coffee.CoffeeStatusState
import com.mgok.conglystore.presentation.user.UploadState
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CoffeeRemoteRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore, private val storage: FirebaseStorage
) : CoffeeRemoteRepository {
    override suspend fun insertCoffeType(coffeeType: CoffeeType): ResultStatusState {
        return try {
            firestore.collection("coffee_types").document(coffeeType.id.toString()).set(coffeeType)
                .await()
            ResultStatusState.Successful
        } catch (e: Exception) {
            Log.e("ghg", "insertCoffeType: $e")
            ResultStatusState.Error
        }
    }

    override suspend fun getListCoffeeType(): CoffeeStatusState {
        return try {
            val snapshot = firestore.collection("coffee_types").get().await()
            val list = snapshot.toObjects(CoffeeType::class.java)
            CoffeeStatusState(
                status = ResultStatusState.Successful, listCoffeeType = list
            )
        } catch (e: Exception) {
            Log.e("ghg", "getCoffeeType: $e")
            CoffeeStatusState(
                status = ResultStatusState.Error,
            )
        }
    }

    override suspend fun deleteCoffeeType(id: String): ResultStatusState {
        return try {
            firestore.collection("coffee_types").document(id).delete().await()
            ResultStatusState.Successful
        } catch (e: Exception) {
            Log.e("ghg", "getDeleteCoffeeType: $e")
            ResultStatusState.Error
        }
    }

    override suspend fun uploadImage(uri: Uri, uid: String): UploadState {
        return try {
            val avatarRef = storage.reference.child("products/$uid.png")
            val uploadTask = avatarRef.putFile(uri).await()
            val url = uploadTask.storage.downloadUrl.await()
            Log.e("ghg", url.toString())
            UploadState(
                status = ResultStatusState.Successful, error = null, url = url
            )
        } catch (e: Exception) {
            UploadState(
                status = ResultStatusState.Successful, error = e.message, url = null
            )
        }
    }

    override suspend fun deleteImage(uri: Uri) {
        try {
            storage.getReferenceFromUrl(uri.toString()).delete().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun insertCoffee(coffee: Coffee): ResultStatusState {
        return try {
            firestore.collection("coffees").document(coffee.id.toString()).set(coffee).await()
            ResultStatusState.Successful
        } catch (e: Exception) {
            Log.e("ghg", "insertCoffeType: $e")
            ResultStatusState.Error
        }
    }

    override suspend fun getListCoffee(): CoffeeStatusState {
        return try {
            val snapshot = firestore.collection("coffees").get().await()
            val list = snapshot.toObjects(Coffee::class.java)
            CoffeeStatusState(
                status = ResultStatusState.Successful, listCoffee = list
            )
        } catch (e: Exception) {
            Log.e("ghg getListCoffee: ", e.toString())
            CoffeeStatusState(status = ResultStatusState.Error)
            CoffeeStatusState()
        }
    }


}