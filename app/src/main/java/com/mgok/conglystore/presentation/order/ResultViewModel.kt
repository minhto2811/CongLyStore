package com.mgok.conglystore.presentation.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgok.conglystore.usecases.bill.UpdatePaymentStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val updatePaymentStatusUseCase: UpdatePaymentStatusUseCase
) : ViewModel() {
    fun updateStatus(billId: String, status: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                updatePaymentStatusUseCase.updateStatus(billId, status)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}