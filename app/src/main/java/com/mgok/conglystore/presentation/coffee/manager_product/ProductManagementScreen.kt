package com.mgok.conglystore.presentation.coffee.manager_product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.mgok.conglystore.component.MyLoadingDialog
import com.mgok.conglystore.component.TopBar
import com.mgok.conglystore.data.remote.coffee.Coffee


@Composable
fun ProductManagementScreen(
    onPop: () -> Unit,
    onNavigate: (String?) -> Unit,
    viewModel: ProductManagementViewModel = hiltViewModel()
) {
    val stateUI by viewModel.stateUI.collectAsState()
    Scaffold(topBar = {
        TopBar(title = "Danh sách sản phẩm", onPop = onPop,
            leading = {
                IconButton(onClick = { onNavigate.invoke(null) }) {

                }
            })
    }
    ) { paddingValues ->
        LaunchedEffect(Unit) {
            viewModel.getData()
        }
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(stateUI.list.size) { index ->
                ProductManagementItem(
                    product = stateUI.list[index],
                    onNavigate = onNavigate
                )
            }
        }
        MyLoadingDialog(visible = stateUI.loading)
    }
}

@Composable
fun ProductManagementItem(product: Coffee, onNavigate: (String) -> Unit) {
    ElevatedCard(
        onClick = { onNavigate(product.id) },
        modifier = Modifier.padding(horizontal = 30.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            SubcomposeAsyncImage(
                model = product.image,
                loading = {
                    CircularProgressIndicator()
                },
                contentDescription = "",
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = product.type,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.Gray
                    )
                )
            }
        }
    }
}
