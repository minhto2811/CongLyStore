package com.mgok.conglystore.models

data class User(
    val uid:String,
    val displayName:String,
    val numberphone:String,
    val avatar:String,
    val birthday: String,
    val role:Int,
    val gender:Int,
)


// role 1 user, 0 admin
//gender 1 = male, 0 = female