package com.mgok.conglystore.presentation.home.tabs.tab_home

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.mgok.conglystore.data.remote.cart.Cart
import com.mgok.conglystore.usecases.cart.UpsertCartUseCase
import com.mgok.conglystore.usecases.coffee.GetListCoffeeByNameUseCase
import com.mgok.conglystore.usecases.coffee_type.GetListCoffeeTypeUseCase
import com.mgok.conglystore.usecases.map.GetLocationByLatlngUseCase
import com.mgok.conglystore.usecases.map.GetLocationCurrentUseCase
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
    private val getListCoffeeByNameUseCase: GetListCoffeeByNameUseCase,
    private val getListCoffeeTypeUseCase: GetListCoffeeTypeUseCase,
    private val upsertCartUseCase: UpsertCartUseCase,
    private val getLocationCurrentUseCase: GetLocationCurrentUseCase,
    private val getInfoUserUseCase: GetInfoUserUseCase,
    private val getLocationByLatlngUseCase: GetLocationByLatlngUseCase
) : ViewModel() {
    private val _stateUI = MutableStateFlow(HomeStateUI())
    val stateUI = _stateUI.asStateFlow()


    var chipState = mutableIntStateOf(0)

    init {
        getListCoffeeType()
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


    fun getLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val location = getLocationCurrentUseCase.getLocationCurrent()
                val line = getLocationByLatlngUseCase.getLocation(LatLng(location.latitude,location.longitude))
                _stateUI.update { it.copy(location = line) }
            } catch (e: Exception) {
                e.printStackTrace()
                _stateUI.update { it.copy(location = "Bật vị trí của thiết bị") }
            }

        }
    }

    private fun getListCoffeeType() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val list = getListCoffeeTypeUseCase()
                _stateUI.update { it.copy(listCoffeeType = list) }
                if (list.isEmpty()) return@launch
                getListCoffeeByName()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getListCoffeeByName() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val coffeeName = _stateUI.value.listCoffeeType[chipState.intValue].name
                val list = getListCoffeeByNameUseCase.getListCoffeeByName(coffeeName)
                _stateUI.update { it.copy(listCoffee = list) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addCart(cart: Cart) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (getInfoUserUseCase() == null) return@launch
                upsertCartUseCase.upsertCart(cart)
            } catch (e: Exception) {
                Log.e("ghg insert: ", e.toString())
            }
        }
    }
}