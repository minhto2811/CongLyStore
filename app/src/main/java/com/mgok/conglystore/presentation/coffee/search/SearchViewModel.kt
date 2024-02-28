package com.mgok.conglystore.presentation.coffee.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgok.conglystore.usecases.coffee.GetListCoffeeByNameUseCase
import com.mgok.conglystore.usecases.coffee.GetListCoffeeUseCase
import com.mgok.conglystore.usecases.coffee_type.GetListCoffeeTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getListCoffeeTypeUseCase: GetListCoffeeTypeUseCase,
    private val getListCoffeeByNameUseCase: GetListCoffeeByNameUseCase,
    private val getListCoffeeUseCase: GetListCoffeeUseCase
) : ViewModel() {

    private val _stateUI = MutableStateFlow(SearchStateUI())
    val stateUI = _stateUI.asStateFlow()


    var coffeeName = mutableStateOf("")
    var query = mutableStateOf("")

    private var job: Job? = null


    init {
        getListCoffeeType()
    }

    private fun getListCoffeeType() {
        viewModelScope.launch(Dispatchers.IO) {
            _stateUI.update { it.copy(loading = true) }
            try {
                val list = getListCoffeeTypeUseCase()
                _stateUI.update { it.copy(listCoffeeType = list) }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _stateUI.update { it.copy(loading = false) }
            }
        }
    }

    fun getListCoffeeByText() {
        if (job?.isCompleted == false) job?.cancel()
        if (job != null) job = null
        job = viewModelScope.launch(Dispatchers.IO) {
            if (query.value.trim().isEmpty()) {
                _stateUI.update { it.copy(listCoffee = emptyList()) }
                return@launch
            }
            delay(200)
            try {
                val list = if (coffeeName.value.trim().isEmpty())
                    getListCoffeeUseCase()
                else
                    getListCoffeeByNameUseCase.getListCoffeeByName(coffeeName.value)
                val data = list.filter {
                    it.type.contains(query.value, true) ||
                            it.name.contains(query.value, true)
                }
                _stateUI.update { it.copy(listCoffee = data) }
            } catch (e: Exception) {
                e.printStackTrace()
                _stateUI.update { it.copy(listCoffee = emptyList()) }
            }
        }
        job?.start()
    }
}