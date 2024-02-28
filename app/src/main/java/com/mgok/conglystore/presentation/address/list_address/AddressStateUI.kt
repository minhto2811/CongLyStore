package com.mgok.conglystore.presentation.address.list_address

import com.mgok.conglystore.data.remote.address.Address

data class AddressStateUI(
    val loading: Boolean = false,
    val listAddress: List<Address> = listOf(),
)
