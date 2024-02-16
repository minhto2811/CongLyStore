package com.mgok.conglystore.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mgok.conglystore.MainActivity
import com.mgok.conglystore.R
import com.mgok.conglystore.Session.setUserSession
import com.mgok.conglystore.presentation.auth.sign_in.SignInViewModel
import com.mgok.conglystore.presentation.user.UserViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    signInViewModel: SignInViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    callback: (String) -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        delay(200)
        var route =  MainActivity.Route.route_auth
        if (signInViewModel.isUserSignin()) {
            val infoUser = userViewModel.getInfoUser()
            route =
                if (infoUser == null) {
                    MainActivity.Route.route_update_user
                } else {
                    setUserSession(infoUser)
                    MainActivity.Route.route_home
                }
        }
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