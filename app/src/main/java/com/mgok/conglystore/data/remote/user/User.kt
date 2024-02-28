package com.mgok.conglystore.data.remote.user

data class User(
    val uid: String = "",
    val displayName: String = "",
    val numberphone: String = "",
    val avatar: String = "",
    val birthday: Long = 1,
    val role: Int = 1,
    val gender: Int = 1,
)


// role 1 user, 0 admin
//gender 1 = male, 0 = female