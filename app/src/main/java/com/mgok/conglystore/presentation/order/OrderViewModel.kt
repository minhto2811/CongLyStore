package com.mgok.conglystore.presentation.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgok.conglystore.data.remote.bill.Bill
import com.mgok.conglystore.data.remote.bill.ItemBill
import com.mgok.conglystore.usecases.address.GetAddressUseCase
import com.mgok.conglystore.usecases.address.GetFistAddressUseCase
import com.mgok.conglystore.usecases.bill.CreateBillUseCase
import com.mgok.conglystore.usecases.cart.DeleteAllBillUseCase
import com.mgok.conglystore.usecases.cart.GetListCartUseCase
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
class OrderViewModel @Inject constructor(
    private val getListCartUseCase: GetListCartUseCase,
    private val getCoffeeByIdUseCase: GetCoffeeByIdUseCase,
    private val getFistAddressUseCase: GetFistAddressUseCase,
    private val getAddressUseCase: GetAddressUseCase,
    private val createBillUseCase: CreateBillUseCase,
    private val deleteAllBillUseCase: DeleteAllBillUseCase
) : ViewModel() {


    private val _stateUI = MutableStateFlow(OrderStateUI())
    val stateUI = _stateUI.asStateFlow()


    fun createBill(billId: String,price:Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val bill = Bill(id = billId,price=price, address = _stateUI.value.address!!)
                _stateUI.value.listCart.forEach {
                    val itemBill = ItemBill(
                        coffeeId = it.idCoffee,
                        image = it.coffee!!.image.toString(),
                        name = it.coffee!!.name,
                        quantity = it.quantity,
                        size = it.size,
                        type = it.coffee!!.type
                    )
                    bill.list.add(itemBill)
                }
                createBillUseCase.createBill(bill)
                deleteAllBillUseCase()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getAddressById(addressId: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val address = when (addressId == null) {
                    true -> getFistAddressUseCase()
                    else -> getAddressUseCase.getAddress(addressId)
                }
                _stateUI.update {
                    it.copy(
                        address = address
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

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
                        listCart = data, totalPrice = totalPrice, loading = false
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _stateUI.update { it.copy(loading = false) }
            }
        }
    }

}