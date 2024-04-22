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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.mgok.conglystore.component.MyElevatedButton
import com.mgok.conglystore.component.MyLoadingDialog
import com.mgok.conglystore.component.MyTextField
import com.mgok.conglystore.utilities.NoRippleInteractionSource

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
        val state by signUpViewModel.state.collectAsState()


        LaunchedEffect(state.message) {
            state.message?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
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
                state = signUpViewModel.email,
                hint = "Nhập địa chỉ email",
                keyboardType = KeyboardType.Email,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                onValidate = { signUpViewModel.validate() }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Mật khẩu",
                style = MaterialTheme.typography.titleSmall,
                color = Color(0xFF2A2A2A)
            )
            Spacer(modifier = Modifier.height(8.dp))
            MyTextField(
                state = signUpViewModel.password,
                hint = "Nhập mật khẩu",
                isPassword = true,
                keyboardType = KeyboardType.Password,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                onValidate = { signUpViewModel.validate() }
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = signUpViewModel.warning,
                style = MaterialTheme.typography.titleSmall,
                color = Color(0xFFEC6767)
            )
            Spacer(modifier = Modifier.height(12.dp))
            MyElevatedButton(title = "Tiếp tục", onClick = {
                signUpViewModel.createAccount()
                focusManager.clearFocus()
            }, enable = signUpViewModel.enableButton)
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


            MyLoadingDialog(visible = state.loading)

        }


    }
}