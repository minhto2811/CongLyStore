package com.mgok.conglystore.presentation.home.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TabHome() {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        TopBar()
    }
}

@Composable
fun TopBar(){
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        OutlinedTextField(value = "",
            onValueChange = {},
            enabled = false,
            label = {
                Text(text = "Tìm kiếm sản phẩm")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Search, contentDescription = "search icon")
            },
            )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun PreviewTabHome() {
    TabHome()
}