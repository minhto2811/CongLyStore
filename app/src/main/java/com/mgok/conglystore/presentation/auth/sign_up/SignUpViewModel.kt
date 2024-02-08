package com.mgok.conglystore.presentation.auth.sign_up

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
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _state = MutableStateFlow(SignUpState())
    val state = _state.asStateFlow()

    fun createAccount(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {

            val signUpState = userRepository.createAccount(email, password)
            _state.update { signUpState }
        }
    }


    fun resetState() {
        _state.update { SignUpState() }
    }

}