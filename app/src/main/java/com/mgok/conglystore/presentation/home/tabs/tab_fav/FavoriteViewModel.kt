package com.mgok.conglystore.presentation.home.tabs.tab_fav

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgok.conglystore.usecases.coffee.GetCoffeeByIdUseCase
import com.mgok.conglystore.usecases.favorite.DeteleFavoriteUseCase
import com.mgok.conglystore.usecases.favorite.GetListFavoriteUseCase
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
class FavoriteViewModel @Inject constructor(
    private val deleteFavoriteUseCase: DeteleFavoriteUseCase,
    private val getListFavoriteUseCase: GetListFavoriteUseCase,
    private val getCoffeeByIdUseCase: GetCoffeeByIdUseCase,
) : ViewModel() {

    private val _stateUI = MutableStateFlow(FavoriteStateUI())
    val stateUI get()= _stateUI.asStateFlow()

     fun getFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(loading = true) }
                val list = getListFavoriteUseCase.getList() ?: return@launch
                val deferredList = list.map { id ->
                    async {
                        getCoffeeByIdUseCase.getById(id)
                    }
                }
                val coffeeList = deferredList.awaitAll().filterNotNull()
                _stateUI.update { it.copy(listCoffee = coffeeList) }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _stateUI.update { it.copy(loading = false) }
            }
        }
    }

    fun deleteFavorite(coffeeId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(loading = true) }
                val list = _stateUI.value.listCoffee.filter { item -> item.id != coffeeId }
                deleteFavoriteUseCase.delete(coffeeId)
                _stateUI.update { it.copy(listCoffee = list) }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _stateUI.update { it.copy(loading = false) }
            }
        }
    }

}