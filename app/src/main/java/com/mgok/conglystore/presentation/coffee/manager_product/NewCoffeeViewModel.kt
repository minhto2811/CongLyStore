package com.mgok.conglystore.presentation.coffee.manager_product

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgok.conglystore.data.remote.coffee.Coffee
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



    init {
        getListCoffeeType()
    }

    fun deleteImage(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                deleteImageUseCase.deleteImage(uri)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun insertCoffee(coffee: Coffee) {
        viewModelScope.launch(Dispatchers.IO) {
            _stateUI.update { it.copy(loading = true) }
            try {
                insertCoffeeUseCase.insertCoffee(coffee)
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
}