package com.mgok.conglystore.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mgok.conglystore.R
import com.mgok.conglystore.data.remote.coffee.Coffee

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    coffee: MutableState<Coffee?>,
    sheetState: SheetState,
    addToCart: (String, String) -> Unit
) {
    if (coffee.value != null) {
        val sizeState = remember(coffee) {
            mutableStateOf(coffee.value!!.sizes[0])
        }
        val anim = mutableStateOf(false)
        ModalBottomSheet(
            onDismissRequest = {
                coffee.value = null
            },
            sheetState = sheetState,
            containerColor = Color.White,
        ) {
            Column(modifier = Modifier.padding(start = 30.dp, end = 30.dp, bottom = 30.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White)
                        .padding(vertical = 8.dp, horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SubcomposeAsyncImage(
                        model = coffee.value?.image,
                        contentDescription = null,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    ) {
                        val statePainter = painter.state
                        if (statePainter is AsyncImagePainter.State.Loading || statePainter is AsyncImagePainter.State.Error) {
                            CircularProgressIndicator()
                        } else {
                            SubcomposeAsyncImageContent()
                        }
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 10.dp)
                    ) {
                        Text(
                            text = coffee.value?.name.toString(),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = coffee.value?.type.toString(),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Text(
                        text = coffee.value?.vote.toString(),
                        style = MaterialTheme.typography.labelLarge
                    )
                    Icon(
                        Icons.Filled.Star, contentDescription = "",
                        tint = Color(0xFFFBBE21)
                    )
                }
                VerticalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp), color = Color.Gray
                )

                ListSizes(sizes = coffee.value?.sizes!!, sizeDefault = sizeState.value.size) {
                    sizeState.value = it
                }

                VerticalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp), color = Color.Gray
                )
                Spacer(modifier = Modifier.height(20.dp))

                if (anim.value) {
                    Spacer(modifier = Modifier.height(12.dp))
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success_animation))
                    LottieAnimation(
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        composition = composition,
                        contentScale = ContentScale.FillHeight,
                    )
                } else {
                    MyElevatedButton(title = "Thêm vào giỏ hàng", onClick = {
                        anim.value = true
                        addToCart.invoke(coffee.value?.id.toString(), sizeState.value.size)
                    })
                }
            }
        }
    }
}