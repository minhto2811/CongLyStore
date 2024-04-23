package com.mgok.conglystore.presentation.auth.sign_in

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.AccessToken
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.mgok.conglystore.MainActivity
import com.mgok.conglystore.usecases.user.GetFireBaseUserUseCase
import com.mgok.conglystore.usecases.user.GetInfoUserUseCase
import com.mgok.conglystore.usecases.user.GoogleAuthUiClientSignInUseCase
import com.mgok.conglystore.usecases.user.HandleFacebookAccessTokenUseCase
import com.mgok.conglystore.usecases.user.LoginWithAccountUseCase
import com.mgok.conglystore.usecases.user.SignInResultUseCase
import com.mgok.conglystore.utilities.isValidEmail
import com.mgok.conglystore.utilities.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInResultUseCase,
    private val loginWithAccountUseCase: LoginWithAccountUseCase,
    private val googleAuthUiClientSignInUseCase: GoogleAuthUiClientSignInUseCase,
    private val handleFacebookAccessTokenUseCase: HandleFacebookAccessTokenUseCase,
    private val getInfoUserUseCase: GetInfoUserUseCase,
    private val getFireBaseUserUseCase: GetFireBaseUserUseCase
) : ViewModel() {


    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val mesage = mutableStateOf("")

    val enableButton by derivedStateOf {
        email.value.isValidEmail() && password.value.isValidPassword()
    }


    init {
        getInfoUser()
    }

    private fun getInfoUser() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.update { it.copy(loading = true) }
                getFireBaseUserUseCase() ?: throw Exception()
                val user = getInfoUserUseCase()
                val route =
                    if (user != null) MainActivity.Route.route_home else MainActivity.Route.route_update_user
                _state.update { it.copy(route = route, loading = false) }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(loading = false) }
            }
        }
    }

    fun tongleDialogPassword() {
        val visibleDialog = !_state.value.visibleDialog
        _state.update { it.copy(visibleDialog = visibleDialog) }
    }


    fun onSignInResult(result: Intent) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.update { it.copy(loading = true) }
                signInUseCase.onSignIn(result)
                getInfoUser()
            } catch (e: Exception) {
                _state.update { it.copy(loading = false, error = e.message) }
            }
        }
    }

    fun loginWithAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.update {
                    it.copy(loading = true)
                }
                loginWithAccountUseCase.loginWithAccount(email.value, password.value)
                getInfoUser()
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                _state.update {
                    it.copy(
                        loading = false,
                        error = "Email hoặc mật khẩu không chính xác"
                    )
                }
            } catch (e: IllegalArgumentException) {
                _state.update {
                    it.copy(
                        loading = false,
                        error = "Không để trống thông tin"
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        loading = false,
                        error = "Đã xảy ra lỗi"
                    )
                }
            }
        }
    }


    fun onGoogleLogin(launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val signInIntentSender = googleAuthUiClientSignInUseCase()
                signInIntentSender?.let {
                    launcher.launch(
                        IntentSenderRequest.Builder(it).build()
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun handleFacebookAccessToken(token: AccessToken) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.update { it.copy(loading = true) }
                handleFacebookAccessTokenUseCase.handleFacebookAccessToken(token)
                getInfoUser()
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(loading = false, error = e.message) }
            }
        }
    }

    fun resetError() {
        _state.update { it.copy(error = null) }
    }
}