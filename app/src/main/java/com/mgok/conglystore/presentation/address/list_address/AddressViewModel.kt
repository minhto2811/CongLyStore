package com.mgok.conglystore.presentation.address.list_address

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgok.conglystore.usecases.address.DeleteAddressUseCase
import com.mgok.conglystore.usecases.address.GetListAddressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val getListAddressUseCase: GetListAddressUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase,
) : ViewModel() {

    private val _stateUI = MutableStateFlow(AddressStateUI())
    val stateUI get() = _stateUI.asStateFlow()

    var addressIdDel by mutableStateOf<String?>(null)
    fun getListAddress() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(loading = true) }
                val list = getListAddressUseCase()
                _stateUI.update { it.copy(listAddress = list) }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _stateUI.update { it.copy(loading = false) }
            }
        }
    }

    fun deleteAddress(addressId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(loading = true) }
                deleteAddressUseCase.deleteAddress(addressId)
                val newList = _stateUI.value.listAddress.filter { it -> it.id != addressId }
                _stateUI.update { it.copy(listAddress = newList) }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _stateUI.update { it.copy(loading = false) }
            }
        }
    }
}