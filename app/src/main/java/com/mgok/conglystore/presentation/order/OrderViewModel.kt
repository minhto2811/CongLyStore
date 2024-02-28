package com.mgok.conglystore.presentation.order

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.mgok.conglystore.data.remote.cart.Cart
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor() :ViewModel() {

    var listCheck = mutableStateListOf<Cart>()
}