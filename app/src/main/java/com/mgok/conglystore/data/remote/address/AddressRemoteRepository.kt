package com.mgok.conglystore.data.remote.address

interface AddressRemoteRepository {
    suspend fun getListAddress(): List<Address>

    suspend fun getAddress(addressId: String):Address?

    suspend fun upsertAddress(address: Address)

    suspend fun deleteAddress(addressId: String)
}