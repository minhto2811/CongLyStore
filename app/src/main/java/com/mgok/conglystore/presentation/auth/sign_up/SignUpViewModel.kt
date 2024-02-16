package com.mgok.conglystore.presentation.auth.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgok.conglystore.data.remote.user.UserRemoteRepositoryImpl
import com.mgok.conglystore.presentation.auth.ResultStatusState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRemoteRepository: UserRemoteRepositoryImpl
) : ViewModel() {
    private val _state = MutableStateFlow(SignUpState())
    val state = _state.asStateFlow()

    fun createAccount(email: String, password: String) {
        _state.update { SignUpState(status = ResultStatusState.Loading) }
        viewModelScope.launch(Dispatchers.IO) {
            val signUpState = userRemoteRepository.createAccount(email, password)
            _state.update { signUpState }
        }
    }

}