package com.mgok.conglystore.presentation.address.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.mgok.conglystore.usecases.map.GetListLocationByNameUseCase
import com.mgok.conglystore.usecases.map.GetLocationByLatlngUseCase
import com.mgok.conglystore.usecases.map.GetLocationCurrentUseCase
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
    private val getListLocationByNameUseCase: GetListLocationByNameUseCase,
    private val getLocationCurrentUseCase: GetLocationCurrentUseCase
) : ViewModel() {

    private val _stateUI = MutableStateFlow(MapStateUI())
    val stateUI = _stateUI.asStateFlow()

    var searchText by mutableStateOf("")
    val visibleDialog = mutableStateOf(false)

    var job: Job? = null


    fun closeMenu() {
        _stateUI.update { it.copy(list = listOf()) }
    }

    init {
        getLocation()
    }

    fun getLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val location = getLocationCurrentUseCase.getLocationCurrent()
                _stateUI.update { it.copy(location = location) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun getListLocationByName() {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            try {
                if (searchText.trim().isEmpty()) {
                    _stateUI.update { it.copy(allow = false) }
                    return@launch
                }
                _stateUI.update { it.copy(loading = true, allow = false) }
                val list = getListLocationByNameUseCase.getLoaction(searchText.trim())
                _stateUI.update { it.copy(loading = false, list = list, allow = true) }
            } catch (e: Exception) {
                e.printStackTrace()
                _stateUI.update { it.copy(loading = false, error = e.message, allow = false) }
            }
        }
    }

    fun getLocationByLatlng(latLng: LatLng) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(loading = true, list = listOf(), allow = false) }
                val line = getLocationByLatlngUseCase.getLocation(latLng)
                searchText = line
                _stateUI.update { it.copy(loading = false, allow = true) }
            } catch (e: Exception) {
                e.printStackTrace()
                _stateUI.update { it.copy(loading = false, error = e.message, allow = false) }
            }
        }
    }
}