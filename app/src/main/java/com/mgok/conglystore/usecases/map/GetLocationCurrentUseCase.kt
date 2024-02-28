package com.mgok.conglystore.usecases.map

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest.*
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class GetLocationCurrentUseCase @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient,
) {


    @SuppressLint("MissingPermission")
    suspend fun getLocationCurrent(): Location {
        return fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        ).await()
    }


}