package com.mgok.conglystore.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Title(title: String) {
    Row(modifier = Modifier.padding(vertical = 16.dp)) {
        Text(text = title)
    }
}
