package com.mgok.conglystore.presentation.user.update

import android.net.Uri
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.mgok.conglystore.data.remote.user.User
import com.mgok.conglystore.usecases.user.DeleteAvatarUseCase
import com.mgok.conglystore.usecases.user.UpdateAvatarUseCase
import com.mgok.conglystore.usecases.user.UpdateAvatarWithLinkUserCase
import com.mgok.conglystore.usecases.user.UpdateInfoUserUseCase
import com.mgok.conglystore.utilities.convertMillisToDate
import com.mgok.conglystore.utilities.isValidFullname
import com.mgok.conglystore.utilities.isValidNumberphone
import com.mgok.conglystore.utilities.removeNonAlphanumericVN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject


@HiltViewModel
class UpdateUserViewModel @Inject constructor(
    private val updateAvatarWithLinkUserCase: UpdateAvatarWithLinkUserCase,
    private val updateAvatarUseCase: UpdateAvatarUseCase,
    private val deleteAvatarUseCase: DeleteAvatarUseCase,
    private val updateInfoUserUseCase: UpdateInfoUserUseCase,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _stateUI = MutableStateFlow(UpdateInfoUserState())
    val stateUI = _stateUI.asStateFlow()


    val displayName = mutableStateOf(removeNonAlphanumericVN(auth.currentUser?.displayName ?: ""))

    var avatar by mutableStateOf(
        auth.currentUser?.photoUrl
            ?: Uri.parse("https://cdn.pixabay.com/photo/2017/07/18/23/23/user-2517433_1280.png")
    )


    val numberphone = mutableStateOf(auth.currentUser?.phoneNumber ?: "")

    var birthday by mutableLongStateOf(Calendar.getInstance().timeInMillis)

    val birthday2 by derivedStateOf {
        mutableStateOf(convertMillisToDate(birthday))
    }

    val gender = mutableIntStateOf(0)


    var visibleDatePickerDialog by mutableStateOf(false)


    var checkBoxState by mutableStateOf(false)


    val enableButton by derivedStateOf {
        displayName.value.isValidFullname() && numberphone.value.isValidNumberphone()
                && checkBoxState && !_stateUI.value.loading
    }

    private val newUser by
    derivedStateOf {
        User(
            uid = auth.currentUser?.uid.toString(),
            displayName = displayName.value,
            avatar = avatar.toString(),
            birthday = birthday,
            gender = gender.intValue,
            numberphone = numberphone.value,
            role = 1
        )
    }


    fun updateAvatarWithLinkOther(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(loading = true) }
                val uri = updateAvatarWithLinkUserCase.updateAvatarWithLinkOther(url)
                _stateUI.update { it.copy(loading = false, url = uri) }
            } catch (e: Exception) {
                e.printStackTrace()
                _stateUI.update { it.copy(loading = false, error = e.message) }
            }
        }
    }

    fun updateAvatar(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(loading = true) }
                val newUri = updateAvatarUseCase.updateAvatar(uri)
                _stateUI.update { it.copy(loading = false, url = newUri) }
            } catch (e: Exception) {
                e.printStackTrace()
                _stateUI.update { it.copy(loading = false, error = e.message) }
            }
        }
    }

    fun deleteAvatar(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                deleteAvatarUseCase.deleteAvatar(uri)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun createInfoUser(callback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(loading = true) }
                updateInfoUserUseCase.createInfoUser(newUser)
                _stateUI.update { it.copy(loading = false) }
                callback()
            } catch (e: Exception) {
                _stateUI.update { it.copy(loading = false, error = e.message) }
            }
        }
    }

}