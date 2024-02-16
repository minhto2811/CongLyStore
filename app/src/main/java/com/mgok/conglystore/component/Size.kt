package com.mgok.conglystore.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mgok.conglystore.data.remote.coffee.Size

@Composable
fun ListSizes(sizes:List<Size>, sizeState: MutableState<Size>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(sizes.size) { index ->
            val item = sizes[index]
            Button(
                onClick = {
                    sizeState.value = item
                },
                modifier = Modifier
                    .size(96.dp, 43.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (item.size == sizeState.value?.size) Color(0xFFFFF5EE) else Color(
                        0xFFFFFFFF
                    ),
                    contentColor = if (item.size == sizeState.value?.size) Color(0xFFC67C4E) else Color(
                        0xFF2F2D2C
                    )
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (item.size == sizeState.value?.size) Color(0xFFC67C4E) else Color(
                        0xFFDEDEDE
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = item.size)
            }

        }
    }
}