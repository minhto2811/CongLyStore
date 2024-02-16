package com.mgok.conglystore.presentation.coffee

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgok.conglystore.data.local.coffee.CoffeeLocalRepository
import com.mgok.conglystore.data.local.coffee.CoffeeType
import com.mgok.conglystore.data.remote.coffee.Coffee
import com.mgok.conglystore.data.remote.coffee.CoffeeRemoteRepository
import com.mgok.conglystore.presentation.auth.ResultStatusState
import com.mgok.conglystore.presentation.user.UploadState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoffeeViewModel @Inject constructor(
    private val coffeeRemoteRepository: CoffeeRemoteRepository,
    private val coffeeLocalRepository: CoffeeLocalRepository
) : ViewModel() {
    private val _state = MutableStateFlow(CoffeeStatusState())
    val state = _state.asStateFlow()

    init {
        getListCoffeeType()
        getListCoffee()
    }

    fun setCoffeeSelected(coffee: Coffee) {
        _state.update { it.copy(coffeeSelected = coffee) }
    }

    private fun getListCoffeeType() {
        _state.update { it.copy(status = ResultStatusState.Loading) }
        viewModelScope.launch(Dispatchers.IO) {
            val newState = coffeeRemoteRepository.getListCoffeeType()
            if (newState.status == ResultStatusState.Successful) {
                _state.update {
                    it.copy(
                        status = ResultStatusState.Default,
                        listCoffeeType = newState.listCoffeeType
                    )
                }
                coffeeLocalRepository.deleteAllCoffeeType()
                coffeeLocalRepository.insertAllCoffeeType(newState.listCoffeeType)
            } else {
                val data = coffeeLocalRepository.getListCoffeeType()
                _state.update {
                    it.copy(
                        status = ResultStatusState.Default,
                        listCoffeeType = data
                    )
                }
            }
        }

    }

    fun insertCoffeeType(coffeeType: CoffeeType) {
        _state.update { it.copy(status = ResultStatusState.Loading) }
        viewModelScope.launch(Dispatchers.IO) {
            val newState = coffeeRemoteRepository.insertCoffeType(coffeeType)
            val data = _state.value.listCoffeeType.toMutableList()
            if (newState == ResultStatusState.Successful) {
                data.add(coffeeType)
                coffeeLocalRepository.insertCoffeeType(coffeeType)
            }
            _state.update { it.copy(status = newState, listCoffeeType = data) }
        }
    }

    fun deleteCoffeeType(coffeeType: CoffeeType) {
        _state.update { it.copy(status = ResultStatusState.Loading) }
        viewModelScope.launch(Dispatchers.IO) {
            val newState = coffeeRemoteRepository.deleteCoffeeType(coffeeType.id.toString())
            val data = _state.value.listCoffeeType.toMutableList()
            if (newState == ResultStatusState.Successful) {
                data.remove(coffeeType)
                coffeeLocalRepository.delete(coffeeType)
            }
            _state.update { it.copy(status = newState, listCoffeeType = data) }
        }
    }

    fun uploadImage(uri: Uri, id: String) {
        _state.update { it.copy(uploadState = UploadState(status = ResultStatusState.Loading)) }
        viewModelScope.launch(Dispatchers.IO) {
            val newState = coffeeRemoteRepository.uploadImage(uri, id)
            _state.update { it.copy(uploadState = newState) }
        }
    }

    fun deleteImage(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            coffeeRemoteRepository.deleteImage(uri)
        }
    }


    fun insertCoffee(coffee: Coffee) {
        _state.update { it.copy(status = ResultStatusState.Loading) }
        viewModelScope.launch(Dispatchers.IO) {
            val newState = coffeeRemoteRepository.insertCoffee(coffee)
            val data = _state.value.listCoffee.toMutableList()
            if (newState == ResultStatusState.Successful) {
                data.add(coffee)
                coffeeLocalRepository.insertCoffee(coffee.toCoffeeLocal())
            }
            _state.update { it.copy(status = newState, listCoffee = data) }
        }
    }

    private fun getListCoffee() {
        _state.update { it.copy(status = ResultStatusState.Loading) }
        viewModelScope.launch(Dispatchers.IO) {
            val coffeeStatusState = coffeeRemoteRepository.getListCoffee()
            if (coffeeStatusState.status == ResultStatusState.Successful) {
                _state.update {
                    it.copy(
                        status = ResultStatusState.Default,
                        listCoffee = coffeeStatusState.listCoffee
                    )
                }
                val list = coffeeStatusState.listCoffee.map { it.toCoffeeLocal() }
                coffeeLocalRepository.insertAllCoffee(list)
            } else {
                val list = coffeeLocalRepository.getListCoffee().map { it.toCoffee() }
                _state.update {
                    it.copy(
                        status = ResultStatusState.Default,
                        listCoffee = list
                    )
                }
            }
        }
    }

}