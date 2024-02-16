package com.mgok.conglystore.presentation.auth.sign_up

import com.mgok.conglystore.presentation.auth.ResultStatusState

data class SignUpState(
    val status: ResultStatusState = ResultStatusState.Default,
    val error: String? = null,
)
