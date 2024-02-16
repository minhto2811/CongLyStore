package com.mgok.conglystore.presentation.auth.reset_password

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mgok.conglystore.component.MyElevatedButton
import com.mgok.conglystore.component.MyTextField
import com.mgok.conglystore.presentation.auth.ResultStatusState
import com.mgok.conglystore.utilities.NoRippleInteractionSource
import com.mgok.conglystore.utilities.isValidEmail

@Composable
fun ResetPassword(
    visible: MutableState<Boolean>,
    resetPasswordViewModel: ResetPasswordViewModel = hiltViewModel(),
    visibleLoading: MutableState<Boolean>,
    timeCountdown: MutableState<Int>
) {
    if (visible.value) {
        Dialog(
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
            onDismissRequest = { }
        ) {
            val cardWidth = LocalConfiguration.current.screenWidthDp * 0.85
            val context = LocalContext.current
            rememberCoroutineScope()

            val state by resetPasswordViewModel.state.collectAsStateWithLifecycle()

            val email = remember {
                mutableStateOf("")
            }


            LaunchedEffect(key1 = state.status) {
                when (state.status) {
                    ResultStatusState.Loading -> {
                        visibleLoading.value = true
                    }

                    else -> {
                        if (state.status == ResultStatusState.Successful)
                            timeCountdown.value = 30
                        visibleLoading.value = false
                        email.value = ""
                        state.message?.let { message ->
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        }
                        resetPasswordViewModel.resetSate()
                    }
                }
            }


            Card(modifier = Modifier.width(cardWidth.dp)) {

                val enableButton = remember {
                    derivedStateOf {
                        email.value.isValidEmail() && timeCountdown.value == 0
                    }
                }

                val titleButton = remember {
                    derivedStateOf {
                        if (timeCountdown.value == 0) "Tiếp tục" else "Tiếp tục sau ${timeCountdown.value}s"
                    }
                }

                val focusRequester = remember { FocusRequester() }
                val focusManager = LocalFocusManager.current

                Column(
                    modifier = Modifier
                        .padding(28.dp)
                        .focusRequester(focusRequester)
                ) {
                    Text(
                        text = "Đặt lại mật khẩu",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF1E1E1E)
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = "Vui lòng nhập email của bạn để đặt lại mật khẩu",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color(0xFF989898)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Địa chỉ email",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color(0xFF2A2A2A)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    MyTextField(
                        state = email,
                        hint = "Nhập địa chỉ email",
                        keyboardType = KeyboardType.Email,
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    )
                    Spacer(modifier = Modifier.height(26.dp))
                    MyElevatedButton(
                        title = titleButton.value,
                        onClick = {
                            resetPasswordViewModel.sendRequestRestPassword(email.value)
                            focusManager.clearFocus()
                        },
                        enable = enableButton
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = "Đóng",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFFEC6767),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable(
                                interactionSource = NoRippleInteractionSource(),
                                indication = null
                            ) {
                                visible.value = false
                            }
                    )
                }
            }
        }
    }
}