package com.mgok.conglystore.presentation.home.tabs.tab_fav

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.mgok.conglystore.MainActivity
import com.mgok.conglystore.Session.getUserSession
import com.mgok.conglystore.data.remote.coffee.Coffee
import com.mgok.conglystore.presentation.coffee.CoffeeViewModel
import com.mgok.conglystore.presentation.user.UserViewModel


@Composable
fun TabFavorite(
    coffeeViewModel: CoffeeViewModel,
    userViewModel: UserViewModel = hiltViewModel(),
    changePage: (String) -> Unit
) {

    val state by coffeeViewModel.state.collectAsState()

    val favorites = remember { mutableStateListOf<Coffee>() }

    LaunchedEffect(Unit) {
        if (getUserSession() != null) {
            favorites.addAll(state.listCoffee.filter { getUserSession()?.favorites?.contains(it.id.toString()) == true })
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp),
    ) {
        items(
            items = favorites,
            key = { it.id }
        ) { coffee ->
            CoffeeItem(coffee = coffee,
                onRemove = {
                    favorites -= coffee
                    userViewModel.removeItemFavorite(coffee.id.toString())
                },
                gotoDetail = {
                    coffeeViewModel.setCoffeeSelected(coffee)
                    changePage.invoke(MainActivity.Route.route_detail_coffe)
                })
            VerticalDivider(modifier = Modifier
                .fillMaxWidth()
                .height(1.dp), color = Color.Gray)
        }
    }
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
        backgroundContent = { Background() },
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

@Composable
@Preview
fun Pre() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(vertical = 8.dp, horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(color = Color.Red)
        ) {

        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp)
        ) {
            Text(text = "Cappucino", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Với socola")
        }
        Text(text = "2.8", style = MaterialTheme.typography.labelLarge)
        Icon(
            Icons.Filled.Star, contentDescription = "",
            tint = Color(0xFFFBBE21)
        )
    }
}

@Composable
fun Background() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
            .padding(end = 10.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(Icons.Default.Delete, contentDescription = "", tint = Color.White)
    }
}

