package com.mgok.conglystore.usecases.cart

import com.mgok.conglystore.data.remote.cart.CartRemoteRepository
import javax.inject.Inject

class DeleteAllBillUseCase @Inject constructor(
    private val cartRemoteRepository: CartRemoteRepository
) {
    operator suspend fun invoke() = cartRemoteRepository.deleteAll()
}