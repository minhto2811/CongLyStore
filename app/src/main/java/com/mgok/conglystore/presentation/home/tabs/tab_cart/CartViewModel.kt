package com.mgok.conglystore.presentation.home.tabs.tab_cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgok.conglystore.data.remote.cart.Cart
import com.mgok.conglystore.usecases.cart.DeleteCartUseCase
import com.mgok.conglystore.usecases.cart.GetListCartUseCase
import com.mgok.conglystore.usecases.cart.UpsertCartUseCase
import com.mgok.conglystore.usecases.coffee.GetCoffeeByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val upsertCartUseCase: UpsertCartUseCase,
    private val getListCartUseCase: GetListCartUseCase,
    private val getCoffeeByIdUseCase: GetCoffeeByIdUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
) : ViewModel() {
    private val _stateUI = MutableStateFlow(CartStateUI())
    val stateUI = _stateUI.asStateFlow()

    fun getListCart() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(loading = true) }
                val list = getListCartUseCase.getListCart()
                var totalPrice = 0f
                val derferred = list.map { cart ->
                    async {
                        val coffee = getCoffeeByIdUseCase.getById(cart.idCoffee)
                        coffee?.let {
                            val price = it.sizes.filter { size -> size.size == cart.size }[0]
                            totalPrice += price.price * cart.quantity
                        }
                        cart.copy(coffee = coffee)
                    }
                }
                val data = derferred.awaitAll().filter { it.coffee != null }
                _stateUI.update {
                    it.copy(
                        listCart = data,
                        totalPrice = getTotalPrice(data),
                        loading = false
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _stateUI.update { it.copy(loading = false) }
            }
        }
    }

    private fun getTotalPrice(list: List<Cart>): Float {
        var total = 0f
        list.forEach { cart ->
            val price = cart.coffee!!.sizes.filter { it.size == cart.size }[0].price
            total += cart.quantity * price
        }
        return total
    }

    fun upsertCart(cart: Cart) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(loading = true) }
                upsertCartUseCase.upsertCart(cart)
                val newListCart = _stateUI.value.listCart.toMutableList()
                newListCart.forEachIndexed { index, item ->
                    if (item.id == cart.id) {
                        newListCart[index] = cart
                        return@forEachIndexed
                    }
                }
                _stateUI.update {
                    it.copy(
                        listCart = newListCart,
                        totalPrice = getTotalPrice(newListCart),
                        loading = false
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _stateUI.update { it.copy(loading = false) }
            }
        }
    }

    fun deleteCart(cartId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(loading = true) }
                deleteCartUseCase.delete(cartId)
                val newListCart = _stateUI.value.listCart.toMutableList()
                newListCart.forEachIndexed { index, item ->
                    if (item.id == cartId) {
                        newListCart.removeAt(index)
                        return@forEachIndexed
                    }
                }
                _stateUI.update {
                    it.copy(
                        listCart = newListCart,
                        totalPrice = getTotalPrice(newListCart),
                        loading = false
                    )
                }
            } catch (e: Exception) {
                Log.e("ghg delete: ", e.toString())
                _stateUI.update { it.copy(loading = false) }
            }
        }
    }
}