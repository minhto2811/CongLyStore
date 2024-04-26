package com.mgok.conglystore.presentation.user.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgok.conglystore.usecases.user.GetInfoUserUseCase
import com.mgok.conglystore.usecases.user.UserSignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val signOutUseCase: UserSignOutUseCase,
    private val getInfoUserUseCase: GetInfoUserUseCase
) : ViewModel() {
    private val _stateUI = MutableStateFlow(SettingsStateUI())
    val stateUI get()= _stateUI.asStateFlow()

    init {
        getInfoUser()
    }

    private fun getInfoUser() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = getInfoUserUseCase()
                _stateUI.update { it.copy(user = user) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                signOutUseCase()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}