package com.mgok.conglystore.presentation.user.refund

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgok.conglystore.usecases.refund.GetListRefundByUserUseCase
import com.mgok.conglystore.usecases.refund.GetListRefundUseCase
import com.mgok.conglystore.usecases.user.GetInfoUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RefundViewModel @Inject constructor(
    private val getListRefundUseCase: GetListRefundUseCase,
    private val getListRefundByUserUseCase: GetListRefundByUserUseCase,
    private val getInfoUserUseCase: GetInfoUserUseCase
) : ViewModel() {
    private val _stateUI = MutableStateFlow(RefundStateUI())
    val stateUI get()= _stateUI.asStateFlow()
    var status by mutableIntStateOf(-1)


    fun getListRefund() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(loading = true) }
                val list = if (getInfoUserUseCase()?.role == 0)
                    getListRefundUseCase.invoke(status)
                else
                    getListRefundByUserUseCase.invoke(status)
                _stateUI.update { it.copy(loading = false, list = list) }
            } catch (e: Exception) {
                e.printStackTrace()
                _stateUI.update { it.copy(loading = false, message = e.toString()) }
            }
        }
    }


}