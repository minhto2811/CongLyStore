package com.mgok.conglystore.presentation.home.tabs.tab_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgok.conglystore.usecases.bill.GetListBillByUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getListBillByUserUseCase: GetListBillByUserUseCase
) : ViewModel() {
    private val _stateUI = MutableStateFlow(HistoryStateUI())
    val stateUI = _stateUI.asStateFlow()

    fun getListBill() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(loading = true) }
                val list = getListBillByUserUseCase()
                _stateUI.update { it.copy(loading = false, listBill = list) }
            } catch (e: Exception) {
                e.printStackTrace()
                _stateUI.update { it.copy(loading = false, message = e.message) }
            }
        }
    }
}