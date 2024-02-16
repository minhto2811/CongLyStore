package com.mgok.conglystore.presentation.auth.sign_up

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mgok.conglystore.MainActivity
import com.mgok.conglystore.component.MyElevatedButton
import com.mgok.conglystore.component.MyLoadingDialog
import com.mgok.conglystore.component.MyTextField
import com.mgok.conglystore.presentation.auth.ResultStatusState
import com.mgok.conglystore.utilities.NoRippleInteractionSource
import com.mgok.conglystore.utilities.isValidEmail
import com.mgok.conglystore.utilities.isValidPassword

@Composable
fun TabSignUp(
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    chagePageIndex: (Int) -> Unit,
    onNavigate: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val context = LocalContext.current
        rememberCoroutineScope()
        val state by signUpViewModel.state.collectAsStateWithLifecycle()

        val email = remember {
            mutableStateOf("")
        }
        val password = remember {
            mutableStateOf("")
        }

        val mesage = remember {
            mutableStateOf("")
        }

        val enableButton = remember {
            derivedStateOf {
                email.value.isValidEmail() && password.value.isValidPassword()
            }
        }

        val visibleDialog = remember {
            mutableStateOf(false)
        }

        LaunchedEffect(key1 = state.status) {
            when (state.status) {
                ResultStatusState.Successful -> {
                    Toast.makeText(context, "Đăng ký thành công", Toast.LENGTH_LONG).show()
                    password.value = ""
                    email.value = ""
                    mesage.value = ""
                    chagePageIndex(0)
                    onNavigate.invoke(MainActivity.Route.route_update_user)
                }

                ResultStatusState.Loading -> {
                    visibleDialog.value = true
                }

                ResultStatusState.Default -> {
                    visibleDialog.value = false
                }

                ResultStatusState.Error -> {
                    Toast.makeText(context, state.error, Toast.LENGTH_LONG).show()
                    mesage.value = state.error.toString()
                }
            }
        }





        LaunchedEffect(key1 = email.value, key2 = password.value) {
            mesage.value =
                if (email.value.isEmpty() || email.value.isValidEmail()) "" else "Email không hợp lệ"
            if (mesage.value.isEmpty())
                mesage.value =
                    if (password.value.isEmpty() || password.value.isValidPassword()) "" else "Mật khẩu nhiều hơn 5 kí tự"


        }

        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester)
        ) {


            Spacer(modifier = Modifier.height(83.dp))
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
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Mật khẩu",
                style = MaterialTheme.typography.titleSmall,
                color = Color(0xFF2A2A2A)
            )
            Spacer(modifier = Modifier.height(8.dp))
            MyTextField(
                state = password,
                hint = "Nhập mật khẩu",
                isPassword = true,
                keyboardType = KeyboardType.Password,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),

                )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = mesage.value,
                style = MaterialTheme.typography.titleSmall,
                color = Color(0xFFEC6767)
            )
            Spacer(modifier = Modifier.height(12.dp))
            MyElevatedButton(title = "Tiếp tục", onClick = {
                signUpViewModel.createAccount(email = email.value, password = password.value)
                focusManager.clearFocus()
            }, enable = enableButton)
            Spacer(modifier = Modifier.height(43.dp))



            Spacer(modifier = Modifier.height(36.dp))

            Text(
                buildAnnotatedString {
                    val spanStyle = SpanStyle(
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        letterSpacing = (-0.5).sp
                    )
                    withStyle(style = spanStyle.copy(color = Color(0xFF989898))) {
                        append("Đã có tài khoản? ")
                    }
                    withStyle(style = spanStyle.copy(color = Color(0xFFC67C4E))) {
                        append("Đăng nhập")
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable(
                        interactionSource = NoRippleInteractionSource(),
                        indication = null
                    ) {
                        chagePageIndex(0)
                    }
            )


            MyLoadingDialog(visible = visibleDialog)

        }


    }
}