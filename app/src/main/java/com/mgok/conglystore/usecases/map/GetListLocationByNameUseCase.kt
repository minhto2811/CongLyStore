package com.mgok.conglystore.usecases.map

import android.location.Address
import android.location.Geocoder
import javax.inject.Inject

class GetListLocationByNameUseCase @Inject constructor(
    private val geocoder: Geocoder
) {
    fun getLoaction(name: String): List<Address> {
        return geocoder.getFromLocationName(name, 5)!!
    }
}