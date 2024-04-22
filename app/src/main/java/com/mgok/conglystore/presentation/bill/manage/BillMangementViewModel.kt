package com.mgok.conglystore.presentation.bill.manage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgok.conglystore.usecases.bill.GetListBillUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class BillMangementViewModel @Inject constructor(
    private val getListBillUseCase: GetListBillUseCase
) : ViewModel() {
    private val _stateUI = MutableStateFlow(BillManagementStateUI())
    val stateUI = _stateUI.asStateFlow()
    var status by mutableIntStateOf(-1)

    fun getListBill() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(loading = true) }
                val list = getListBillUseCase.invoke(status)
                _stateUI.update { it.copy(loading = false, list = list) }
            } catch (e: Exception) {
                e.printStackTrace()
                _stateUI.update { it.copy(loading = false, message = e.localizedMessage) }
            }
        }
    }

}