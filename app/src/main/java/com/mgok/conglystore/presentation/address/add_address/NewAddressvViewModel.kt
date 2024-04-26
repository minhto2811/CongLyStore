package com.mgok.conglystore.presentation.address.add_address

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgok.conglystore.data.remote.address.Address
import com.mgok.conglystore.usecases.address.GetAddressUseCase
import com.mgok.conglystore.usecases.address.UpsertAddressUseCase
import com.mgok.conglystore.utilities.isValidFullname
import com.mgok.conglystore.utilities.isValidNumberphone
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class NewAddressvViewModel @Inject constructor(
    private val upsertAddressUseCase: UpsertAddressUseCase,
    private val getAddressUseCase: GetAddressUseCase
) : ViewModel() {

    private val _stateUI = MutableStateFlow(NewAddressStateUI())

    val stateUI get() = _stateUI.asStateFlow()


    val displayName = mutableStateOf("")
    val numberPhone = mutableStateOf("")
    var addressId: String? = null


    val location = mutableStateOf("")


    val enableButton by derivedStateOf {
        displayName.value.isValidFullname() && numberPhone.value.isValidNumberphone()
                && location.value.isNotEmpty() && !_stateUI.value.loading
    }


    fun upsertAddress() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(loading = true) }
                val address = Address(
                    id = addressId ?: UUID.randomUUID().toString(),
                    displayName = displayName.value,
                    location = location.value,
                    numberPhone = numberPhone.value,
                )
                upsertAddressUseCase.upsertAddress(address)
                displayName.value = ""
                numberPhone.value = ""
                location.value = ""
                _stateUI.update {
                    it.copy(
                        loading = false,
                        message = if (addressId == null) "Thêm thành công" else "Cập nhât thành công"
                    )
                }
                addressId = null
            } catch (e: Exception) {
                e.printStackTrace()
                _stateUI.update { it.copy(loading = false, message = e.message) }
            }
        }
    }

    fun getAddressById() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(loading = true) }
                val address = getAddressUseCase.getAddress(addressId.toString())
                address?.let { add ->
                    displayName.value = add.displayName
                    numberPhone.value = add.numberPhone
                    location.value = add.location
                }
                _stateUI.update { it.copy(loading = false) }
            } catch (e: Exception) {
                e.printStackTrace()
                _stateUI.update { it.copy(loading = false, message = e.message) }
            }
        }
    }

}