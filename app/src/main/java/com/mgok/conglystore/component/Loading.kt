package com.mgok.conglystore.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mgok.conglystore.R


@Composable
fun MyLoadingDialog(
    visible: MutableState<Boolean>,
) {
    if (visible.value) {
        Dialog(onDismissRequest = {}) {
            Card(
                modifier = Modifier
                    .size(120.dp)
            ) {
                val composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_animation)).value
                LottieAnimation(
                    composition = composition,
                    iterations = Int.MAX_VALUE
                )
            }
        }
    }
}