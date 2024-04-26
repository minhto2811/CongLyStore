package com.mgok.conglystore.presentation.auth.sign_up

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.mgok.conglystore.usecases.user.CreateAccountUseCase
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
class SignUpViewModel @Inject constructor(
    private val createAccountUseCase: CreateAccountUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(SignUpState())
    val state get() = _state.asStateFlow()


    val email = mutableStateOf("")
    val password = mutableStateOf("")
    var warning by mutableStateOf("")

    val enableButton by derivedStateOf {
        email.value.isValidEmail() && password.value.isValidPassword()
    }


    fun validateEmail() {
        if (email.value.isNotEmpty() && !email.value.isValidEmail()) {
            warning = "Email không hợp lệ"
        } else {
            warning = ""
        }

    }

    fun validatePassword() {
        if (password.value.isNotEmpty() && !password.value.isValidPassword()) {
            warning = "Mật khẩu nhiều hơn 5 kí tự"
        } else {
            warning = ""
        }

    }

    fun createAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.update { it.copy(loading = true) }
                createAccountUseCase.createAccount(email.value, password.value)
                _state.update { it.copy(loading = false, message = "Tạo tài khoản thành công") }
                email.value = ""
                password.value = ""
                warning = ""
            } catch (e: FirebaseAuthWeakPasswordException) {
                _state.update {
                    it.copy(
                        loading = false,
                        message = "Mật khẩu nhiều hơn 5 kí tự"
                    )
                }
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                _state.update {
                    it.copy(
                        loading = false,
                        message = "Email không hợp lệ"
                    )
                }
            } catch (e: IllegalArgumentException) {
                _state.update {
                    it.copy(
                        loading = false,
                        message = "Hãy điền đầy đủ thông tin"
                    )
                }
            } catch (e: FirebaseAuthUserCollisionException) {
                _state.update {
                    it.copy(
                        loading = false,
                        message = "Email đã được đăng ký"
                    )
                }
            }
        }
    }

}