package com.mgok.conglystore.presentation.coffee.manager_product

import android.net.Uri
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgok.conglystore.data.remote.coffee.Coffee
import com.mgok.conglystore.data.remote.coffee.Size
import com.mgok.conglystore.usecases.coffee.InsertCoffeeUseCase
import com.mgok.conglystore.usecases.coffee_type.GetListCoffeeTypeUseCase
import com.mgok.conglystore.usecases.image.DeleteImageUseCase
import com.mgok.conglystore.usecases.image.UploadImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewCoffeeViewModel @Inject constructor(
    private val getListCoffeeTypeUseCase: GetListCoffeeTypeUseCase,
    private val uploadImageUseCase: UploadImageUseCase,
    private val insertCoffeeUseCase: InsertCoffeeUseCase,
    private val deleteImageUseCase: DeleteImageUseCase,
) : ViewModel() {

    private val _stateUI = MutableStateFlow(NewCoffeeStateUI())
    val stateUI = _stateUI.asStateFlow()


    var nameCoffee = mutableStateOf("")
    val typeCoffee = mutableStateOf("")
    var imageCoffee by mutableStateOf<String?>(null)
    val sizes = mutableStateListOf<Size>()

    val showDialog = mutableStateOf(false)

    val size = mutableStateOf("")
    val price = mutableStateOf("")

    val coffee by derivedStateOf {
        mutableStateOf(
            Coffee(
                name = nameCoffee.value,
                type = typeCoffee.value,
                image = imageCoffee,
                sizes = sizes
            )
        )
    }

    val enableButton by derivedStateOf {
        mutableStateOf(
            nameCoffee.value.isNotEmpty()
                    && typeCoffee.value.isNotEmpty()
                    && !imageCoffee.isNullOrEmpty()
                    && sizes.isNotEmpty()
        )
    }


    init {
        getListCoffeeType()
    }

    fun addSize() {
        try {
            val count = sizes.indexOfFirst { it.size == size.value.trim() }
            if (count > -1){
                _stateUI.update { it.copy(error = "Kích thước đã tồn tại") }
                return
            }
            val newSize = Size(
                size = size.value,
                price = price.value.toFloat()
            )
            sizes.add(newSize)
            size.value = ""
            price.value = ""
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }

    fun deleteImage() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                deleteImageUseCase.deleteImage(imageCoffee.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun insertCoffee() {
        viewModelScope.launch(Dispatchers.IO) {
            _stateUI.update { it.copy(loading = true) }
            try {
                insertCoffeeUseCase.insertCoffee(coffee.value)
                typeCoffee.value = ""
                imageCoffee = null
                sizes.clear()
            } catch (e: Exception) {
                e.printStackTrace()
                _stateUI.update { it.copy(error = e.message) }
            } finally {
                _stateUI.update { it.copy(loading = false) }
            }
        }
    }

    fun uploadImage(uri: Uri, uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _stateUI.update { it.copy(loading = true) }
            try {
                val url = uploadImageUseCase.uploadImage(uri, uid)
                _stateUI.update { it.copy(url = url) }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _stateUI.update { it.copy(loading = false) }
            }
        }
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

    fun deleteItemSize(size: String) {
        sizes.removeIf { it.size == size }
    }
}