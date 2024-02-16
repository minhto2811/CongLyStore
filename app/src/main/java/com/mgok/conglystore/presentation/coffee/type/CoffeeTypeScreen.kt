package com.mgok.conglystore.presentation.coffee.type

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.mgok.conglystore.component.MyElevatedButton
import com.mgok.conglystore.component.MyLoadingDialog
import com.mgok.conglystore.component.MyTextField
import com.mgok.conglystore.data.local.coffee.CoffeeType
import com.mgok.conglystore.presentation.auth.ResultStatusState
import com.mgok.conglystore.presentation.coffee.CoffeeViewModel
import com.mgok.conglystore.utilities.NoRippleInteractionSource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CoffeeTypeScreen(
    coffeeViewModel: CoffeeViewModel,
) {
    val name = remember {
        mutableStateOf("")
    }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val enableButton = remember {
        derivedStateOf {
            name.value.trim().isNotEmpty()
        }
    }
    val state by coffeeViewModel.state.collectAsState()

    val visibleLoading = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = state.status) {
        when (state.status) {
            ResultStatusState.Loading -> {
                visibleLoading.value = true
            }

            ResultStatusState.Successful -> {
                visibleLoading.value = false
                name.value = ""
            }

            ResultStatusState.Default -> {
                visibleLoading.value = false
            }

            ResultStatusState.Error -> {
                visibleLoading.value = false
            }

        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .focusRequester(focusRequester)
            .padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            MyTextField(
                state = name, hint = "Nhập tên loại cà phê",
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                capitalization = KeyboardCapitalization.Words,
                hasSpace = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            MyElevatedButton(
                title = "Thêm mới",
                onClick = {
                    val coffeeType = CoffeeType(name = name.value.trim())
                    coffeeViewModel.insertCoffeeType(coffeeType)
                    focusManager.clearFocus()
                }, enable = enableButton
            )
            Spacer(modifier = Modifier.height(16.dp))

        }
        items(state.listCoffeeType.size) {
            val item = state.listCoffeeType[it]
            Text(
                text = item.name,
                modifier = Modifier
                    .size(width = 376.dp, height = 56.dp)
                    .padding(vertical = 16.dp)
                    .combinedClickable(
                        indication = null,
                        interactionSource = NoRippleInteractionSource(),
                        onLongClick = {
                            coffeeViewModel.deleteCoffeeType(item)
                        },
                        onClick = {

                        }
                    )
            )
        }
    }

    MyLoadingDialog(visible = visibleLoading)
}