package com.mgok.conglystore.data.remote.bill

import com.mgok.conglystore.data.remote.address.Address
import java.util.Date
import java.util.UUID

data class Bill(
    val id: String = UUID.randomUUID().toString(),
    val userId: String = "",
    val price: Long = 0L,
    var paymentStatus: Int = -1,
    var status: Int = -1,
    val list: ArrayList<ItemBill> = arrayListOf(),
    val address: Address = Address(),
    val date: Long = Date().time,
    var delete: Boolean = false
)

/* status
* -1 cho xac nhan
* 0 don hang se dc giao
* 1 da giao hang
* 2 huy don
* */

data class ItemBill(
    val coffeeId: String = "",
    val image: String = "",
    val size: String = "",
    val quantity: Int = 0,
    val name: String = "",
    val type: String = ""
)
