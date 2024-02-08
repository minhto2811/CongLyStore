package com.mgok.conglystore.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mgok.conglystore.R
import com.mgok.conglystore.presentation.auth.sign_in.SignInViewModel
import com.mgok.conglystore.presentation.auth.user.UserViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    signInViewModel: SignInViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    callback: (String) -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        var route = ""
        if (signInViewModel.isUserSignin()) {
            userViewModel.checkNewUser { isNew ->
                route =
                    if (isNew) context.getString(R.string.route_update_user) else context.getString(
                        R.string.route_settings
                    )
            }
        } else {
            route = context.getString(R.string.route_auth)
        }
        delay(2000)
        callback.invoke(route)
    }
    val composition: LottieComposition? =
        rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.sign_in_animation)).value
    val widthScreen = LocalConfiguration.current.screenWidthDp
    val widthLottie = widthScreen * 0.8
    Box(
        modifier = Modifier
            .size(widthLottie.dp)
    ) {
        LottieAnimation(
            composition = composition,
            iterations = Int.MAX_VALUE,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}