package com.mgok.conglystore.presentation.auth.sign_in

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.AccessToken
import com.mgok.conglystore.data.remote.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {


    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun isUserSignin(): Boolean {
        return userRepository.userCurrent() != null
    }

    fun onSignInResult(result: Intent) {
        viewModelScope.launch(Dispatchers.IO) {
            val signInState = userRepository.onSignInResult(result)
            _state.update { signInState }
        }
    }

    fun loginWithAccount(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val signInState = userRepository.loginWithAccount(email, password)
            _state.update { signInState }
        }
    }

    fun resetState() {
        _state.update { SignInState() }
    }

    suspend fun googleAuthUiClientSignIn() = userRepository.googleAuthUiClientSignIn()

    fun handleFacebookAccessToken(token: AccessToken) {
        viewModelScope.launch(Dispatchers.IO) {
            val signInState = userRepository.handleFacebookAccessToken(token)
            _state.update { signInState }
        }
    }
}