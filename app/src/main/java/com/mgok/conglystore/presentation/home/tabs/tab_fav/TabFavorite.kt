package com.mgok.conglystore.presentation.home.tabs.tab_fav

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.mgok.conglystore.component.BackgroundDelete
import com.mgok.conglystore.component.MyLoadingDialog
import com.mgok.conglystore.data.remote.coffee.Coffee


@Composable
fun TabFavorite(
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    changePage: (String) -> Unit
) {

    val stateUI by favoriteViewModel.stateUI.collectAsState()


    LaunchedEffect(Unit) {
        favoriteViewModel.getFavorite()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp),
    ) {
        items(
            items = stateUI.listCoffee,
            key = { it.id }
        ) { coffee ->
            CoffeeItem(coffee = coffee,
                onRemove = {
                    favoriteViewModel.deleteFavorite(coffee.id)
                },
                gotoDetail = {
                    changePage.invoke("detail_coffee/${coffee.id}")
                })
            VerticalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp), color = Color.Gray
            )
        }
    }
    MyLoadingDialog(visible = stateUI.loading)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeeItem(
    coffee: Coffee,
    onRemove: () -> Unit,
    gotoDetail: () -> Unit
) {
    val width = LocalConfiguration.current.screenWidthDp
    val state = rememberSwipeToDismissBoxState(
        positionalThreshold = with(LocalDensity.current) {
            { (width * 0.8).dp.toPx() }
        },
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onRemove.invoke()
                true
            } else {
                false
            }
        }
    )
    SwipeToDismissBox(
        state = state,
        backgroundContent = { BackgroundDelete() },
        enableDismissFromStartToEnd = false,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                gotoDetail.invoke()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(vertical = 8.dp, horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubcomposeAsyncImage(
                model = coffee.image,
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
                Text(text = coffee.name, style = MaterialTheme.typography.bodyLarge)
                Text(text = coffee.type, style = MaterialTheme.typography.bodyMedium)
            }
            Text(text = coffee.vote.toString(), style = MaterialTheme.typography.labelLarge)
            Icon(
                Icons.Filled.Star, contentDescription = "",
                tint = Color(0xFFFBBE21)
            )
        }

    }

}



