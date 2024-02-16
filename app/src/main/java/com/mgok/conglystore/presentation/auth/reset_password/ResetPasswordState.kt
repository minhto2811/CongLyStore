package com.mgok.conglystore.presentation.auth.reset_password

import com.mgok.conglystore.presentation.auth.ResultStatusState

data class ResetPasswordState(
    val status: ResultStatusState = ResultStatusState.Default,
    val message: String? = null
)
