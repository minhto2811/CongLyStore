package com.mgok.conglystore.presentation.user

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgok.conglystore.Session.addItemFavoriteUserSession
import com.mgok.conglystore.Session.removeItemFavoriteUserSession
import com.mgok.conglystore.Session.setUserSession
import com.mgok.conglystore.data.remote.user.User
import com.mgok.conglystore.data.remote.user.UserRemoteRepository
import com.mgok.conglystore.presentation.auth.ResultStatusState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRemoteRepository: UserRemoteRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UpdateInfoUserState())
    val state = _state.asStateFlow()


    fun addItemFavorite(idCoffee: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userRemoteRepository.addItemFavorite(idCoffee)
            addItemFavoriteUserSession(idCoffee)
        }
    }

    fun removeItemFavorite(idCoffee: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userRemoteRepository.removeItemFavorite(idCoffee)
            removeItemFavoriteUserSession(idCoffee)
        }
    }

    suspend fun getInfoUser() = userRemoteRepository.getInfoUser()

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            userRemoteRepository.signOut()
            setUserSession(null)
        }
    }

    fun updateAvatarWithLinkOther(url: String, uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val uploadAvatarState = userRemoteRepository.updateAvatarWithLinkOther(url, uid)
            _state.update { it.copy(uploadState = uploadAvatarState) }
        }
    }

    fun updateAvatar(uri: Uri, uid: String) {
        _state.update {
            it.copy(uploadState = UploadState(status = ResultStatusState.Loading))
        }
        viewModelScope.launch(Dispatchers.IO) {
            val uploadAvatarState = userRemoteRepository.updateAvatar(uri, uid)
            _state.update { it.copy(uploadState = uploadAvatarState) }
        }
    }

    fun deleteAvatar(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            userRemoteRepository.deleteAvatar(uri)
        }
    }

    fun createInfoUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            val updateInfoUserState = userRemoteRepository.createInfoUser(user)
            _state.update { updateInfoUserState }
        }
    }

    fun resetState() {
        _state.update { UpdateInfoUserState() }
    }
}