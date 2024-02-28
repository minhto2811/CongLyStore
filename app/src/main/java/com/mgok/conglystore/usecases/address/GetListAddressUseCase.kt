package com.mgok.conglystore.usecases.address

import com.mgok.conglystore.data.remote.address.AddressRemoteRepository
import javax.inject.Inject

class GetListAddressUseCase @Inject constructor(
    private val addressRemoteRepository: AddressRemoteRepository
) {
    suspend operator fun invoke() = addressRemoteRepository.getListAddress()
}