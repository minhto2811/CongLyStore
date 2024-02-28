package com.mgok.conglystore.usecases.address

import com.mgok.conglystore.data.remote.address.AddressRemoteRepository
import javax.inject.Inject

class DeleteAddressUseCase @Inject constructor(
    private val addressRemoteRepository: AddressRemoteRepository
) {
    suspend fun deleteAddress(addressId: String) = addressRemoteRepository.deleteAddress(addressId)
}