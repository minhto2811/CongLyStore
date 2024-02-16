package com.mgok.conglystore.presentation.auth.sign_in

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.AccessToken
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
class SignInViewModel @Inject constructor(
    private val userRemoteRepository: UserRemoteRepositoryImpl
) : ViewModel() {


    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()


    fun resetState() {
        _state.update { SignInState() }
    }

    fun isUserSignin(): Boolean {
        return userRemoteRepository.userCurrent() != null
    }

    fun onSignInResult(result: Intent) {
        _state.update { SignInState(status = ResultStatusState.Loading) }
        viewModelScope.launch(Dispatchers.IO) {
            val signInState = userRemoteRepository.onSignInResult(result)
            _state.update { signInState }
        }
    }

    fun loginWithAccount(email: String, password: String) {
        _state.update { SignInState(status = ResultStatusState.Loading) }
        viewModelScope.launch(Dispatchers.IO) {
            val signInState = userRemoteRepository.loginWithAccount(email, password)
            _state.update { signInState }
        }
    }


    suspend fun googleAuthUiClientSignIn() = userRemoteRepository.googleAuthUiClientSignIn()

    fun handleFacebookAccessToken(token: AccessToken) {
        _state.update { SignInState(status = ResultStatusState.Loading) }
        viewModelScope.launch(Dispatchers.IO) {
            val signInState = userRemoteRepository.handleFacebookAccessToken(token)
            _state.update { signInState }
        }
    }
}