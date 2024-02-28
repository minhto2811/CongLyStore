package com.mgok.conglystore.presentation.coffee.manager_coffee_type

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mgok.conglystore.component.MyElevatedButton
import com.mgok.conglystore.component.MyLoadingDialog
import com.mgok.conglystore.component.MyTextField
import com.mgok.conglystore.utilities.NoRippleInteractionSource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CoffeeTypeScreen(
    coffeeTypeViewModel: CoffeeTypeViewModel = hiltViewModel()
) {

    val stateUI by coffeeTypeViewModel.stateUI.collectAsState()

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current





    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .focusRequester(focusRequester)
            .padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            MyTextField(
                state = coffeeTypeViewModel.name, hint = "Nhập tên loại cà phê",
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
                    coffeeTypeViewModel.insertCoffeeType()
                    focusManager.clearFocus()
                }, enable = coffeeTypeViewModel.enableButton
            )
            Spacer(modifier = Modifier.height(16.dp))

        }
        items(stateUI.listCoffeeType.size) {
            val item = stateUI.listCoffeeType[it]
            Text(
                text = item.name,
                modifier = Modifier
                    .size(width = 376.dp, height = 56.dp)
                    .padding(vertical = 16.dp)
                    .combinedClickable(
                        indication = null,
                        interactionSource = NoRippleInteractionSource(),
                        onLongClick = {
                            coffeeTypeViewModel.deleteCoffeeType(item)
                        },
                        onClick = {

                        }
                    )
            )
        }
    }

    MyLoadingDialog(visible = stateUI.loading)
}