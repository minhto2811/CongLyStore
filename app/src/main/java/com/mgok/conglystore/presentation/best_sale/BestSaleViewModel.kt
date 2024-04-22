package com.mgok.conglystore.presentation.best_sale

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgok.conglystore.usecases.coffee.GetListCoffeeBySold
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BestSaleViewModel @Inject constructor(
    private val getListCoffeeBySold: GetListCoffeeBySold
) : ViewModel() {
    private val _stateUI = MutableStateFlow(BestSaleStateUI())
    val stateUI get() = _stateUI.asStateFlow()

    fun getListCoffee() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(loading = true) }
                val list = getListCoffeeBySold()
                _stateUI.update { it.copy(loading = false, list = list) }
            } catch (e: Exception) {
                e.printStackTrace()
                _stateUI.update { it.copy(loading = false, message = "Lỗi") }
            }
        }
    }
}