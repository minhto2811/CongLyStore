package com.mgok.conglystore.presentation.auth.sign_in

data class SignInState(
    val loginSuccess: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null,
    val visibleDialog: Boolean = false,
    val route: String? = null
)