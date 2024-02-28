package com.mgok.conglystore.presentation.address.map

import android.location.Address


data class MapStateUI(
    val loading: Boolean = false,
    val list: List<Address> = listOf(),
    val error: String? = null,
)