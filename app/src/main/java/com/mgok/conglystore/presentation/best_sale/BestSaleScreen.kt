package com.mgok.conglystore.presentation.best_sale

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.mgok.conglystore.MainActivity
import com.mgok.conglystore.component.MyLoadingDialog
import com.mgok.conglystore.component.TopBar
import com.mgok.conglystore.data.remote.coffee.Coffee

@Composable
fun BestSaleScreen(
    onPop: () -> Unit,
    onNavigate:(String) -> Unit,
    bestSaleViewModel: BestSaleViewModel = hiltViewModel()
) {

    val stateUI by bestSaleViewModel.stateUI.collectAsState()

    LaunchedEffect(Unit) {
        bestSaleViewModel.getListCoffee()
    }
    Scaffold(
        topBar = { TopBar(title = "Bán chạy", onPop = onPop) }
    ) { paddingValue ->
        LazyColumn(
            modifier = Modifier.padding(paddingValue),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(stateUI.list.size) { index ->
                val item = stateUI.list[index]
                BestSaleItem(
                    coffee = item,
                    position = index + 1
                ) {
                    onNavigate.invoke("${MainActivity.Route.route_detail_coffee}/coffeeId=${item.id}")
                }
            }
        }
        MyLoadingDialog(visible = stateUI.loading)
    }
}

@Composable
fun BestSaleItem(coffee: Coffee, position: Int, onClick: () -> Unit) {
    ElevatedButton(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = position.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Black
                ),
                modifier = Modifier.padding(end = 12.dp)
            )
            SubcomposeAsyncImage(
                model = coffee.image,
                loading = {
                    CircularProgressIndicator()
                },
                contentDescription = "",
                modifier = Modifier.size(44.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp)
            ) {
                Text(
                    text = coffee.name,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.Black
                    )
                )
                Text(
                    text = coffee.type,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.Gray
                    )
                )
            }
            Text(
                text = "${coffee.sold}",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.Gray
                )
            )
        }
    }
}
