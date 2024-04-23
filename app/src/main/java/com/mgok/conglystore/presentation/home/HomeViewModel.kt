package com.mgok.conglystore.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgok.conglystore.usecases.user.GetInfoUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getInfoUserUseCase: GetInfoUserUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(HomeStateUI())
    val state get() = _state.asStateFlow()

    init {
        getInfoUser()
    }

    private fun getInfoUser() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = getInfoUserUseCase()
                _state.update { it.copy(role = user?.role ?: 1) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}