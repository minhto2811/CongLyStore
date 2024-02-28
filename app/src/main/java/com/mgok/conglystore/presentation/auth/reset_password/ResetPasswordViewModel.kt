package com.mgok.conglystore.presentation.auth.reset_password

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgok.conglystore.usecases.user.ResetPasswordUseCase
import com.mgok.conglystore.utilities.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

    private val _stateUI = MutableStateFlow(ResetPasswordState())
    val stateUI = _stateUI.asStateFlow()

    val email = mutableStateOf("")

    val enableButton by
    derivedStateOf {
        email.value.isValidEmail()
    }


    fun sendRequestRestPassword() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(loading = true, message = null) }
                resetPasswordUseCase.sendRequestResetPassword(email.value)
                email.value = ""
                _stateUI.update { it.copy(loading = false, message = "Kiểm tra email của bạn") }
            } catch (e: Exception) {
                e.printStackTrace()
                _stateUI.update { it.copy(loading = false, message = "Đã xảy ra lỗi") }
            }
        }
    }
}