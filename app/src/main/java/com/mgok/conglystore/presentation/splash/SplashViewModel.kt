package com.mgok.conglystore.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgok.conglystore.MainActivity
import com.mgok.conglystore.usecases.user.GetFireBaseUserUseCase
import com.mgok.conglystore.usecases.user.GetInfoUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getInfoUserUseCase: GetInfoUserUseCase,
    private val getFireBaseUserUseCase: GetFireBaseUserUseCase
) : ViewModel() {

    private val _route = MutableStateFlow<String?>(null)
    val route = _route.asStateFlow()


     fun checkOut() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (getFireBaseUserUseCase() == null) {
                    _route.update { MainActivity.Route.route_auth }
                    return@launch
                }
                val user = getInfoUserUseCase()
                if (user != null)
                    _route.update { MainActivity.Route.route_home }
                else
                    _route.update { MainActivity.Route.route_update_user }
            } catch (e: Exception) {
                e.printStackTrace()
                _route.update { MainActivity.Route.route_auth }
            }
        }
    }
}