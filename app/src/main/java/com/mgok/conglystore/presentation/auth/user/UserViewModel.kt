package com.mgok.conglystore.presentation.auth.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgok.conglystore.data.remote.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    fun checkNewUser(callback: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.checkNewUser(callback)
        }
    }

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.signOut()
        }

    }
}