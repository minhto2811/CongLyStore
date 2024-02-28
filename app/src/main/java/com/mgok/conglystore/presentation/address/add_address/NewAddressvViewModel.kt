package com.mgok.conglystore.presentation.address.add_address

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgok.conglystore.data.remote.address.Address
import com.mgok.conglystore.usecases.address.UpsertAddressUseCase
import com.mgok.conglystore.utilities.isValidFullname
import com.mgok.conglystore.utilities.isValidNumberphone
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewAddressvViewModel @Inject constructor(
    private val upsertAddressUseCase: UpsertAddressUseCase
) : ViewModel() {

    private val _stateUI = MutableStateFlow(NewAddressStateUI())
    val stateUI = _stateUI.asStateFlow()


    val displayName = mutableStateOf("")
    val numberPhone = mutableStateOf("")

    val location = mutableStateOf("")

    var latitue by mutableDoubleStateOf(21.0272256)

    var longtiteu by mutableDoubleStateOf(105.7718272)


    val enableButton by derivedStateOf {
        displayName.value.isValidFullname() && numberPhone.value.isValidNumberphone()
                && location.value.isNotEmpty() && !_stateUI.value.loading
    }


    fun upsertAddress() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(loading = true) }
                val address = Address(
                    displayName = displayName.value,
                    location = location.value,
                    numberPhone = numberPhone.value,
                    longtitue = longtiteu,
                    latitue = latitue
                )
                upsertAddressUseCase.upsertAddress(address)
                displayName.value = ""
                numberPhone.value = ""
                location.value = ""
                _stateUI.update { it.copy(loading = false, message = "Thêm thành công") }
            } catch (e: Exception) {
                e.printStackTrace()
                _stateUI.update { it.copy(loading = false, message = e.message) }
            }
        }
    }

}