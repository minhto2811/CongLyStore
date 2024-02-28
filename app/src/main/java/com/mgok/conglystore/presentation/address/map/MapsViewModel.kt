package com.mgok.conglystore.presentation.address.map

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.mgok.conglystore.usecases.map.GetListLocationByNameUseCase
import com.mgok.conglystore.usecases.map.GetLocationByLatlngUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val getLocationByLatlngUseCase: GetLocationByLatlngUseCase,
    private val getListLocationByNameUseCase: GetListLocationByNameUseCase
) : ViewModel() {

    private val _stateUI = MutableStateFlow(MapStateUI())
    val stateUI = _stateUI.asStateFlow()

    var searchText by mutableStateOf("")

    var job: Job? = null


    fun closeMenu() {
        _stateUI.update { it.copy(list = listOf()) }
    }


    fun getListLocationByName() {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            try {
                if (searchText.trim().isEmpty()) return@launch
                _stateUI.update { it.copy(loading = true) }
                val list = getListLocationByNameUseCase.getLoaction(searchText.trim())
                list.forEach { Log.e("getListLocationByName: ", it.getAddressLine(0)) }
                _stateUI.update { it.copy(loading = false, list = list) }
            } catch (e: Exception) {
                e.printStackTrace()
                _stateUI.update { it.copy(loading = false, error = e.message) }
            }
        }
    }

    fun getLocationByLatlng(latLng: LatLng) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(loading = true, list = listOf()) }
                val line = getLocationByLatlngUseCase.getLocation(latLng)
                searchText = line
                _stateUI.update { it.copy(loading = false) }
            } catch (e: Exception) {
                e.printStackTrace()
                _stateUI.update { it.copy(loading = false, error = e.message) }
            }
        }
    }
}