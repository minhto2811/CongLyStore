package com.mgok.conglystore.presentation.address.add_address

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mgok.conglystore.MainActivity
import com.mgok.conglystore.component.MyElevatedButton
import com.mgok.conglystore.component.MyLoadingDialog
import com.mgok.conglystore.component.MyTextField
import com.mgok.conglystore.component.TopBar

@Composable
fun NewAddressScreen(
    onPop: () -> Unit,
    addressId: String?,
    stringAddress: String?,
    changePage: (String) -> Unit,
    viewModel: NewAddressvViewModel = hiltViewModel()
) {
    val stateUI by viewModel.stateUI.collectAsStateWithLifecycle()

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.addressId = addressId
    }

    LaunchedEffect(stateUI.message) {
        stateUI.message?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
    }

    LaunchedEffect(stringAddress) {
        stringAddress?.let {
            viewModel.location.value = it
        }
    }

    LaunchedEffect(addressId) {
        addressId?.let {
            viewModel.getAddressById(it)
        }
    }


    MyLoadingDialog(visible = stateUI.loading)
    Scaffold(
        topBar = {
            TopBar(if (addressId == null) "Thêm địa chỉ" else "Cập nhật địa chỉ", onPop)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester)
                .padding(start = 30.dp, end = 30.dp, top = paddingValues.calculateTopPadding())
        ) {

            Text(
                text = "Họ tên",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 5.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            MyTextField(
                state = viewModel.displayName,
                hint = "Nhập tên người liên lạc",
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                hasSpace = true,
                capitalization = KeyboardCapitalization.Words,
                onValidate = {
                    //
                },
                maxChar = 40
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Số điện thoại",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 5.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            MyTextField(
                state = viewModel.numberPhone,
                hint = "Nhập số điện thoại",
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                hasSpace = true,
                keyboardType = KeyboardType.Phone,
                capitalization = KeyboardCapitalization.Words,
                onValidate = {
                    //
                },
                maxChar = 10
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Địa chỉ", style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 5.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            MyTextField(
                state = viewModel.location,
                hint = "Chọn địa chỉ",
                keyboardActions = KeyboardActions { },
                hasSpace = true,
                capitalization = KeyboardCapitalization.Words,
                enableb = false,
                onClickable = {
                    changePage.invoke(MainActivity.Route.route_map)
                }
            )

            Spacer(modifier = Modifier.height(26.dp))

            MyElevatedButton(
                title = if (addressId == null) "Thêm địa chỉ" else "Cập nhật địa chỉ",
                onClick = { viewModel.upsertAddress() },
                enable = viewModel.enableButton
            )

        }
    }
}
