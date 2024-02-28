package com.mgok.conglystore.presentation.coffee.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgok.conglystore.data.remote.cart.Cart
import com.mgok.conglystore.data.remote.coffee.Size
import com.mgok.conglystore.usecases.cart.UpsertCartUseCase
import com.mgok.conglystore.usecases.coffee.GetCoffeeByIdUseCase
import com.mgok.conglystore.usecases.favorite.AddFavoriteUseCase
import com.mgok.conglystore.usecases.favorite.DeteleFavoriteUseCase
import com.mgok.conglystore.usecases.favorite.GetListFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailCoffeeViewModel @Inject constructor(
    private val getListFavoriteUseCase: GetListFavoriteUseCase,
    private val getCoffeeByIdUseCase: GetCoffeeByIdUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val deleteFavoriteUseCase: DeteleFavoriteUseCase,
    private val upsertCartUseCase: UpsertCartUseCase,
) : ViewModel() {

    private val _stateUI = MutableStateFlow(DetailCoffeeStateUI())
    val stateUI = _stateUI.asStateFlow()

    fun getCoffeeById(coffeeId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _stateUI.update { it.copy(loading = true) }
            try {
                val coffee = getCoffeeByIdUseCase.getById(coffeeId)
                _stateUI.update { it.copy(coffee = coffee, size = coffee?.sizes?.get(0)) }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _stateUI.update { it.copy(loading = false) }
            }
        }
    }

    fun checkFavorite(idCoffee: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val list = getListFavoriteUseCase.getList()
                val isFavorite = list?.contains(idCoffee) ?: false
                _stateUI.update { it.copy(isFavorite = isFavorite) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(isFavorite = true) }
                addFavoriteUseCase.add(_stateUI.value.coffee?.id.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            _stateUI.update { it.copy(isFavorite = false) }
            try {
                deleteFavoriteUseCase.delete(_stateUI.value.coffee?.id.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addCart() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val cart = Cart(
                    idCoffee = _stateUI.value.coffee?.id.toString(),
                    size = _stateUI.value.size?.size.toString()
                )
                upsertCartUseCase.upsertCart(cart)
            } catch (e: Exception) {
                Log.e("ghg insert: ", e.toString())
            }
        }
    }

    fun updateSizeSelected(size: Size) {
        _stateUI.update { it.copy(size = size) }
    }
}