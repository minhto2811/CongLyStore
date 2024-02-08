package com.mgok.conglystore.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Line1() {
    Box(modifier = Modifier.size(width = 98.dp, height = 2.dp).background(color = Color(0xFFE1E1E1)))
}