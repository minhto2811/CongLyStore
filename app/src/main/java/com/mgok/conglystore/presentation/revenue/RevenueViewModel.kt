package com.mgok.conglystore.presentation.revenue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgok.conglystore.usecases.bill.GetListBillByDateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RevenueViewModel @Inject constructor(
    private val getListBillByDateUseCase: GetListBillByDateUseCase
) : ViewModel() {

    private val _stateUI = MutableStateFlow(RevenueStateUI())
    val stateUI get() = _stateUI.asStateFlow()

    var type = 0



    fun getListBillByDate(startDate: Long, endDate: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(loading = true) }
                val list = getListBillByDateUseCase(startDate, endDate)
                val total = list.sumOf { it.price }
                _stateUI.update { it.copy(loading = false, data = total) }
            } catch (e: Exception) {
                e.printStackTrace()
                _stateUI.update { it.copy(loading = false, message = e.localizedMessage) }
            }
        }
    }
}