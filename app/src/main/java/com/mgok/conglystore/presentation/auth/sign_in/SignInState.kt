package com.mgok.conglystore.presentation.auth.sign_in

import com.mgok.conglystore.presentation.auth.ResultStatusState

data class SignInState(
    val status: ResultStatusState = ResultStatusState.Default,
    val error: String? = null,
)