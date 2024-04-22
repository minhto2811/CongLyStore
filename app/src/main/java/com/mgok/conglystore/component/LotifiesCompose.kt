package com.mgok.conglystore.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LotifiesCompose(
    resourceId: Int,
    modifier: Modifier = Modifier,
    iterations: Int = Int.MAX_VALUE
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resourceId))
    LottieAnimation(
        modifier = modifier, composition = composition,
        iterations = iterations,
        contentScale = ContentScale.Crop,
    )
}