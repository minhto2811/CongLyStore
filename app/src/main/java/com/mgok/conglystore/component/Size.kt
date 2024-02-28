package com.mgok.conglystore.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mgok.conglystore.data.remote.coffee.Size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ListSizes(
    sizes: List<Size>,
    sizeDefault: String?,
    scale: Float = 1f,
    changeSize: ((Size) -> Unit)? = null
) {

    val scope = rememberCoroutineScope()
    val lazyState = rememberLazyListState()

    val sizeState by remember(sizeDefault) {
        mutableStateOf(sizeDefault?:sizes[0].size)
    }
    LazyRow(
        state = lazyState,
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        items(sizes.size) { index ->
            val item = sizes[index]
            if (item.size == sizeState) {
                scope.launch(Dispatchers.Main) {
                    lazyState.animateScrollToItem(index)
                }
            }

            val containerColor =
                if (item.size == sizeState) Color(0xFFFFF5EE) else Color(0xFFFFFFFF)
            val contentColor =
                if (item.size == sizeState) Color(0xFFC67C4E) else Color(0xFF2F2D2C)
            val boderColor =
                if (item.size == sizeState) Color(0xFFC67C4E) else Color(0xFFDEDEDE)

            Box(
                modifier = Modifier
                    .size((96 * scale).dp, (43 * scale).dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = containerColor, shape = RoundedCornerShape(12.dp))
                    .border(
                        width = 1.dp,
                        color = boderColor,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable {
                        if (item.size != sizeState){
                            changeSize?.invoke(item)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.size,
                    color = contentColor,
                    modifier = Modifier.scale(scale)
                )
            }

        }
    }
}