package com.mgok.conglystore.presentation.auth.sign_in

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.mgok.conglystore.R
import com.mgok.conglystore.component.MyOutlineButton

@Composable
fun FacebookButton(
    callback: (AccessToken?) -> Unit
) {
    val loginManager = LoginManager.getInstance()
    val callbackManager = remember { CallbackManager.Factory.create() }
    val launcher = rememberLauncherForActivityResult(
        loginManager.createLogInActivityResultContract(callbackManager, null)
    ) {
        // nothing to do. handled in FacebookCallback
    }

    DisposableEffect(Unit) {
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onCancel() {
                // do nothing
            }

            override fun onError(error: FacebookException) {
                Log.e("FacebookButton", error.toString())
                callback.invoke(null)
            }

            override fun onSuccess(result: LoginResult) {
                val accessToken = result.accessToken
                callback.invoke(accessToken)
            }
        })

        onDispose {
            loginManager.unregisterCallback(callbackManager)
        }
    }



    MyOutlineButton(
        onClick = {
            launcher.launch(listOf("email", "public_profile"))
        },
        idLogoResource = R.drawable.logo_facebook,
        title = "Đăng nhập bằng Facebook"
    )
}
