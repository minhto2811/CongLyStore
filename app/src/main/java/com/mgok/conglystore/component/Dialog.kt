package com.mgok.conglystore.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mgok.conglystore.R


@Composable
fun MyLoadingDialog(
    visible: Boolean
) {
    if (visible) {
        Dialog(onDismissRequest = {}) {
            Card(
                modifier = Modifier
                    .size(120.dp)
            ) {
                val composition =
                    rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_animation)).value
                LottieAnimation(
                    composition = composition,
                    iterations = Int.MAX_VALUE
                )
            }
        }
    }
}


@Composable
fun RequiresPermissionDialog(visible: MutableState<Boolean>, callback: () -> Unit) {
    if (visible.value) {
        Dialog(onDismissRequest = {}) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(color = Color.White, shape = RoundedCornerShape(12.dp))
                    .padding(30.dp)
            ) {
                val composition =
                    rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.location_animation)).value
                LottieAnimation(
                    composition = composition,
                    iterations = Int.MAX_VALUE,
                    modifier = Modifier.size(100.dp)
                )
                Text(text = "Ứng dụng yêu cầu quyền vị trí...")
                Box(modifier = Modifier
                    .background(
                        color = Color(0xFFC67C4E),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        callback.invoke()
                    }
                    .padding(horizontal = 24.dp, vertical = 12.dp)) {
                    Text(
                        text = "Xác nhận",
                        color = Color.White
                    )
                }
            }
        }
    }
}
