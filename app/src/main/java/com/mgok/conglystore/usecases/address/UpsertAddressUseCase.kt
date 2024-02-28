package com.mgok.conglystore.usecases.address

import com.mgok.conglystore.data.remote.address.Address
import com.mgok.conglystore.data.remote.address.AddressRemoteRepository
import javax.inject.Inject

class UpsertAddressUseCase @Inject constructor(
    private val addressRemoteRepository: AddressRemoteRepository
) {
    suspend fun upsertAddress(address: Address) = addressRemoteRepository.upsertAddress(address)
}