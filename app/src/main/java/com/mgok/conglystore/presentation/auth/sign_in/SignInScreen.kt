package com.mgok.conglystore.presentation.auth.sign_in

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
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
import com.mgok.conglystore.R
import com.mgok.conglystore.component.Line1
import com.mgok.conglystore.component.MyElevatedButton
import com.mgok.conglystore.component.MyLoadingDialog
import com.mgok.conglystore.component.MyOutlineButton
import com.mgok.conglystore.component.MyTextField
import com.mgok.conglystore.presentation.auth.reset_password.ResetPassword
import com.mgok.conglystore.presentation.auth.reset_password.ResetPasswordViewModel
import com.mgok.conglystore.presentation.auth.user.UserViewModel
import com.mgok.conglystore.utils.isValidEmail
import com.mgok.conglystore.utils.isValidPassword
import kotlinx.coroutines.launch


@Composable
fun TabSignIn(
    signInViewModel: SignInViewModel = hiltViewModel(),
    resetPasswordViewModel: ResetPasswordViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    chagePageIndex: (Int) -> Unit,
    onNavigate: (String) -> Unit,
) {
    val state = signInViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == ComponentActivity.RESULT_OK) {
                result.data?.let { signInViewModel.onSignInResult(it) }
            }
        }
    )
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

    val visibleResetPassword = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = state.value.isSignInSuccessful) {
        if (state.value.isSignInSuccessful) {
            userViewModel.checkNewUser { isNew ->
                val route =
                    if (isNew) context.getString(R.string.route_update_user) else context.getString(
                        R.string.route_settings
                    )
                onNavigate.invoke(route)
            }
            mesage.value = ""
            signInViewModel.resetState()
            visibleDialog.value = false
        }

    }



    LaunchedEffect(key1 = state.value.signInError) {
        state.value.signInError?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            mesage.value = error
            visibleDialog.value = false
            signInViewModel.resetState()
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = mesage.value,
                style = MaterialTheme.typography.titleSmall,
                color = Color(0xFFEC6767)
            )
            Text(
                text = "Quên mật khẩu?",
                style = MaterialTheme.typography.titleSmall,
                color = Color(0xFF648DDB),
                modifier = Modifier.clickable {
                    visibleResetPassword.value = true
                }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        MyElevatedButton(
            title = "Tiếp tục", onClick = {
                signInViewModel.loginWithAccount(email.value, password.value)
                focusManager.clearFocus()
                visibleDialog.value = true
            },
            enable = enableButton
        )
        Spacer(modifier = Modifier.height(43.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Line1()
            Text(
                text = "hoặc",
                style = MaterialTheme.typography.titleSmall,
                color = Color(0xFFE1E1E1),
                modifier = Modifier.padding(horizontal = 14.dp)
            )
            Line1()

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
                coroutineScope.launch {
                    val signInIntentSender = signInViewModel.googleAuthUiClientSignIn()
                    signInIntentSender?.let {
                        launcher.launch(
                            IntentSenderRequest.Builder(it).build()
                        )
                    }
                }
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
                withStyle(style = spanStyle.copy(color = Color(0xFF648DDB))) {
                    append("Đăng ký")
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    chagePageIndex.invoke(1)
                }
        )


    }


    ResetPassword(
        visible = visibleResetPassword,
        resetPasswordViewModel = resetPasswordViewModel,
        visibleLoading = visibleDialog
    )

    MyLoadingDialog(visible = visibleDialog)

}
