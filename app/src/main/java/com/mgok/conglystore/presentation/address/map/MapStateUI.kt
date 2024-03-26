package com.mgok.conglystore.presentation.address.map

import android.location.Address
import android.location.Location


data class MapStateUI(
    val loading: Boolean = false,
    val list: List<Address> = listOf(),
    val error: String? = null,
    val allow:Boolean = false,
    val location: Location? = null
)