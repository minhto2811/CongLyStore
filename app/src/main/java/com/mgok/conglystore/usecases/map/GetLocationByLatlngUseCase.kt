package com.mgok.conglystore.usecases.map

import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class GetLocationByLatlngUseCase @Inject constructor(
    private val geocoder: Geocoder
) {
    suspend fun getLocation(latLng: LatLng): String {
        return geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)!![0]
            .getAddressLine(0)
    }
}