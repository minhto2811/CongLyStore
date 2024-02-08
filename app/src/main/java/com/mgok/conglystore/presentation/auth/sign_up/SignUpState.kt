package com.mgok.conglystore.presentation.auth.sign_up

data class SignUpState(
    val isSignUpSuccessful: Boolean = false,
    val signUpError: String? = null
)
