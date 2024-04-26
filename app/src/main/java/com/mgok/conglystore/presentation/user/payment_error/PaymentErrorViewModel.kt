package com.mgok.conglystore.presentation.user.payment_error

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgok.conglystore.usecases.error_payment.GetListErrorPaymentByUserUseCase
import com.mgok.conglystore.usecases.error_payment.GetListErrorPaymentUseCase
import com.mgok.conglystore.usecases.user.GetInfoUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentErrorViewModel @Inject constructor(
    private val getListErrorPaymentByUserUseCase: GetListErrorPaymentByUserUseCase,
    private val getInfoUserUseCase: GetInfoUserUseCase,
    private val getListErrorPaymentUseCase: GetListErrorPaymentUseCase
) : ViewModel() {
    private val _stateUI = MutableStateFlow(PMStateUI())
    val stateUI get()= _stateUI.asStateFlow()
    var status by mutableIntStateOf(-1)


    fun getListPaymentError() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(loading = true) }
                val list = if (getInfoUserUseCase()?.role == 0) {
                    getListErrorPaymentUseCase.getAll(status)
                } else {
                    getListErrorPaymentByUserUseCase.selectByStatus(status)
                }
                _stateUI.update { it.copy(loading = false, list = list) }
            } catch (e: Exception) {
                e.printStackTrace()
                _stateUI.update { it.copy(loading = false, message = e.localizedMessage) }
            }
        }
    }
}