package com.mgok.conglystore.usecases.address

import com.mgok.conglystore.data.remote.address.AddressRemoteRepository
import javax.inject.Inject

class GetFistAddressUseCase @Inject constructor(
    private val addressRemoteRepository: AddressRemoteRepository
) {
    suspend operator fun invoke() = addressRemoteRepository.getFirstAddress()
}