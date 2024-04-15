package com.mgok.conglystore.data.remote.bill

import com.mgok.conglystore.data.remote.address.Address
import java.util.UUID

data class Bill(
    val id: String = UUID.randomUUID().toString(),
    val userId: String = "",
    val price: Long = 0L,
    var paymentStatus: Int = -1,
    var status: Int = 0,
    val list: ArrayList<ItemBill> = arrayListOf(),
    val address: Address = Address(),
    var delete: Boolean = false
)

data class ItemBill(
    val coffeeId: String = "",
    val image: String = "",
    val size: String = "",
    val quantity: Int = 0,
    val name: String = "",
    val type: String = ""
)
