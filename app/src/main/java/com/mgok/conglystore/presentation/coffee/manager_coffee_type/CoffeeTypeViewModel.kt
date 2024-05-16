package com.mgok.conglystore.presentation.coffee.manager_coffee_type

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgok.conglystore.data.remote.coffee_type.CoffeeType
import com.mgok.conglystore.usecases.coffee.DeleteCoffeeByTypeUseCase
import com.mgok.conglystore.usecases.coffee_type.DeleteCoffeeTypeUseCase
import com.mgok.conglystore.usecases.coffee_type.GetListCoffeeTypeUseCase
import com.mgok.conglystore.usecases.coffee_type.InsertCoffeeTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoffeeTypeViewModel @Inject constructor(
    private val insertCoffeeTypeUseCase: InsertCoffeeTypeUseCase,
    private val deleteCoffeeTypeUseCase: DeleteCoffeeTypeUseCase,
    private val getListCoffeeTypeUseCase: GetListCoffeeTypeUseCase,
    private val deleteCoffeeByTypeUseCase: DeleteCoffeeByTypeUseCase
) : ViewModel() {

    private val _stateUI = MutableStateFlow(CoffeeTypeStateUI())
    val stateUI get() = _stateUI.asStateFlow()

    val name = mutableStateOf("")

    val enableButton by derivedStateOf {
        name.value.trim().isNotEmpty()
    }

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

    fun insertCoffeeType() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(loading = true) }
                val coffeeType = CoffeeType(name = name.value.trim())
                insertCoffeeTypeUseCase.insertCoffeeType(coffeeType)
                val list = _stateUI.value.listCoffeeType.toMutableList()
                list.add(coffeeType)
                _stateUI.update { it.copy(listCoffeeType = list) }
                name.value = ""
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _stateUI.update { it.copy(loading = false) }
            }
        }
    }

    fun deleteCoffeeType(coffeeType: CoffeeType) {
        viewModelScope.launch(Dispatchers.IO) {
            _stateUI.update { it.copy(loading = true) }
            try {
                deleteCoffeeTypeUseCase.delete(coffeeType.id)
                val list = _stateUI.value.listCoffeeType.toMutableList()
                list.remove(coffeeType)
                _stateUI.update { it.copy(listCoffeeType = list) }
                deleteCoffeeByTypeUseCase.invoke(coffeeType.name)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _stateUI.update { it.copy(loading = false) }
            }
        }
    }
}