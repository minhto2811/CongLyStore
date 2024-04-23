package com.mgok.conglystore.presentation.coffee.manager_product

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgok.conglystore.usecases.coffee.FilterCoffeebyQuerryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductManagementViewModel @Inject constructor(
    private val filterCoffeebyQuerryUseCase: FilterCoffeebyQuerryUseCase
) : ViewModel() {
    private val _stateUI = MutableStateFlow(ProductManagementStateUI())
    val stateUI get() = _stateUI.asStateFlow()

    val querry = mutableStateOf("")

    fun getData(isLoading:Boolean = true) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(loading = isLoading) }
                val list = filterCoffeebyQuerryUseCase.filter(querry.value)
                Log.e( "getData: ",list.size.toString() )
                _stateUI.update { it.copy(loading = false, list = list) }
            } catch (e: Exception) {
                e.printStackTrace()
                _stateUI.update { it.copy(loading = false, message = e.toString()) }
            }
        }
    }
}