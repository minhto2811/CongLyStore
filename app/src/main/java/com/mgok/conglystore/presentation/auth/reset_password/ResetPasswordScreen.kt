package com.mgok.conglystore.presentation.auth.reset_password

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
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
import com.mgok.conglystore.component.MyElevatedButton
import com.mgok.conglystore.component.MyTextField
import com.mgok.conglystore.utilities.NoRippleInteractionSource

@Composable
fun ResetPasswordScreen(
    resetPasswordViewModel: ResetPasswordViewModel = hiltViewModel(),
    timeCountdown: MutableState<Int>,
    closeDialog: () -> Unit
) {
    Dialog(
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
        onDismissRequest = { }
    ) {
        val cardWidth = LocalConfiguration.current.screenWidthDp * 0.85
        val context = LocalContext.current
        rememberCoroutineScope()

        val stateUI by resetPasswordViewModel.stateUI.collectAsState()




        LaunchedEffect(key1 = stateUI.message) {
            stateUI.message?.let { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
        }


        Card(modifier = Modifier.width(cardWidth.dp)) {


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
                    state = resetPasswordViewModel.email,
                    hint = "Nhập địa chỉ email",
                    keyboardType = KeyboardType.Email,
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                )
                Spacer(modifier = Modifier.height(26.dp))
                if (stateUI.loading) {
                    Box(
                        modifier = Modifier
                            .size(width = 376.dp, height = 56.dp)
                            .background(
                                color = Color(0xFFC67C4E),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color.White
                        )
                    }
                } else {
                    MyElevatedButton(
                        title = titleButton.value,
                        onClick = {
                            resetPasswordViewModel.sendRequestRestPassword()
                            focusManager.clearFocus()
                        },
                        enable = resetPasswordViewModel.enableButton && timeCountdown.value == 0
                    )
                }
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = "Đóng",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (stateUI.loading) Color(0x80EC6767) else Color(0xFFEC6767),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable(
                            interactionSource = NoRippleInteractionSource(),
                            indication = null
                        ) {
                            if (!stateUI.loading) closeDialog()
                        }
                )
            }
        }
    }
}