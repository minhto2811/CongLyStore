package com.mgok.conglystore.presentation.auth.sign_in

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.mgok.conglystore.R
import com.mgok.conglystore.component.MyElevatedButton
import com.mgok.conglystore.component.MyLoadingDialog
import com.mgok.conglystore.component.MyOutlineButton
import com.mgok.conglystore.component.MyTextField
import com.mgok.conglystore.presentation.auth.reset_password.ResetPasswordScreen
import com.mgok.conglystore.utilities.NoRippleInteractionSource


@Composable
fun TabSignIn(
    signInViewModel: SignInViewModel = hiltViewModel(),
    timeCountdown: MutableState<Int>,
    chagePageIndex: (Int) -> Unit,
    onNavigate: (String) -> Unit,
) {
    val state by signInViewModel.state.collectAsState()
    val context = LocalContext.current


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == ComponentActivity.RESULT_OK) {
                result.data?.let { signInViewModel.onSignInResult(it) }
            }
        }
    )

    LaunchedEffect(state.route) {
        state.route?.let {
            onNavigate.invoke(it)
            signInViewModel.resetError()
        }
    }




    LaunchedEffect(key1 = state.error) {
        state.error?.let { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
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
            state = signInViewModel.email,
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
            state = signInViewModel.password,
            hint = "Nhập mật khẩu",
            isPassword = true,
            keyboardType = KeyboardType.Password,
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),

            )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = signInViewModel.mesage.value,
                style = MaterialTheme.typography.titleSmall,
                color = Color(0xFFEC6767)
            )
            Text(
                text = "Quên mật khẩu?",
                style = MaterialTheme.typography.titleSmall,
                color = Color(0xFFC67C4E),
                modifier = Modifier.clickable(
                    interactionSource = NoRippleInteractionSource(),
                    indication = null
                ) {
                    signInViewModel.tongleDialogPassword()
                },
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        MyElevatedButton(
            title = "Tiếp tục", onClick = {
                signInViewModel.loginWithAccount()
                focusManager.clearFocus()
            },
            enable = signInViewModel.enableButton
        )
        Spacer(modifier = Modifier.height(43.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(width = 98.dp, height = 2.dp)
                    .background(color = Color(0xFFE1E1E1))
            )
            Text(
                text = "hoặc",
                style = MaterialTheme.typography.titleSmall,
                color = Color(0xFFE1E1E1),
                modifier = Modifier.padding(horizontal = 14.dp)
            )
            Box(
                modifier = Modifier
                    .size(width = 98.dp, height = 2.dp)
                    .background(color = Color(0xFFE1E1E1))
            )

        }
        Spacer(modifier = Modifier.height(43.dp))

        FacebookButton { accessToken ->
            if (accessToken != null) {
                signInViewModel.handleFacebookAccessToken(accessToken)
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        MyOutlineButton(
            onClick = {
                signInViewModel.onGoogleLogin(launcher)
            },
            idLogoResource = R.drawable.logo_google,
            title = "Đăng nhập bằng Google"
        )

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
                    append("Bạn không có tài khoản? ")
                }
                withStyle(style = spanStyle.copy(color = Color(0xFFC67C4E))) {
                    append("Đăng ký")
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable(
                    interactionSource = NoRippleInteractionSource(),
                    indication = null
                ) {
                    chagePageIndex.invoke(1)
                }
        )


    }

    if (state.visibleDialog) {
        ResetPasswordScreen(timeCountdown = timeCountdown) {
            signInViewModel.tongleDialogPassword()
        }
    }


    MyLoadingDialog(visible = state.loading)

}
