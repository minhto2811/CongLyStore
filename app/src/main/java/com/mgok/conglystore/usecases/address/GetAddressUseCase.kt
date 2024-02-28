package com.mgok.conglystore.usecases.address

import com.mgok.conglystore.data.remote.address.AddressRemoteRepository
import javax.inject.Inject

class GetAddressUseCase @Inject constructor(
    private val addressRemoteRepository: AddressRemoteRepository
) {
    suspend fun getAddress(addressId: String) = addressRemoteRepository.getAddress(addressId)
}