package com.mgok.conglystore.Session

import com.mgok.conglystore.data.remote.user.User

private var _userSession: User? = null

fun setUserSession(user: User?) {
    _userSession = user
}

fun getUserSession() = _userSession

fun addItemFavoriteUserSession(idCoffee:String){
    _userSession?.favorites?.add(idCoffee)
}

fun removeItemFavoriteUserSession(idCoffee:String){
    _userSession?.favorites?.remove(idCoffee)
}