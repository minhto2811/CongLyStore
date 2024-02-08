package com.mgok.conglystore.presentation.auth.reset_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgok.conglystore.data.remote.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ResetPasswordState())
    val state = _state.asStateFlow()

    fun resetState() {
        _state.update { ResetPasswordState() }
    }


    fun sendRequestRestPassword(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val resetPasswordState = userRepository.sendRequestRestPassword(email)
            _state.update { resetPasswordState }
        }
    }
}