package com.mgok.conglystore.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mgok.conglystore.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel = hiltViewModel(),
    callback: (String) -> Unit
) {
    val route by splashViewModel.route.collectAsState()

    LaunchedEffect(route) {
        delay(1000)
        route?.let { callback.invoke(it) }
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