package com.mgok.conglystore.presentation.home.tabs.tab_home

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.mgok.conglystore.MainActivity
import com.mgok.conglystore.R
import com.mgok.conglystore.Session.getUserSession
import com.mgok.conglystore.component.BottomSheet
import com.mgok.conglystore.component.CoffeeItem
import com.mgok.conglystore.presentation.coffee.CoffeeStatusState
import com.mgok.conglystore.presentation.coffee.CoffeeViewModel
import com.mgok.conglystore.presentation.map.MapViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabHome(
    mapViewModel: MapViewModel,
    coffeeViewModel: CoffeeViewModel,
    drawerState: DrawerState,
    changePage: (String) -> Unit,
) {

    val stateCoffee by coffeeViewModel.state.collectAsState()
    val stateMap by mapViewModel.state.collectAsState()


    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val launcherLocationPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        val check = permissions.values.reduce { acc, next -> acc && next }
        if (check) {
            mapViewModel.getLocationCurrent(context)
        } else {
            Toast.makeText(context, "Quyền vị trí bị từ chối", Toast.LENGTH_SHORT).show()
        }
    }

    val locationPermission = arrayOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
    )

    LaunchedEffect(key1 = Unit) {
        if (locationPermission.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }) {
            mapViewModel.getLocationCurrent(context)
        } else {
            launcherLocationPermission.launch(locationPermission)
        }

    }


    val width = LocalConfiguration.current.screenWidthDp
    LocalConfiguration.current.screenHeightDp
    val coroutineScope = rememberCoroutineScope()


    val colorStops = arrayOf(
        0f to Color(0xFF313131),
        1f to Color(0xFF131313)
    )
    val chipState = remember {
        mutableStateOf("")
    }

    val listProduct = remember {
        derivedStateOf {
            stateCoffee.listCoffee.filter { it.name == chipState.value }
        }
    }

    LaunchedEffect(key1 = stateCoffee.listCoffeeType) {
        if (stateCoffee.listCoffeeType.isNotEmpty()) {
            chipState.value = stateCoffee.listCoffeeType[0].name
        }
    }


    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
    ) {
        item(
            span = { GridItemSpan(2) }
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {

                val (boxRef, avatarRef, searchRef,
                    bannerRef, chipRef, menuRef)
                        = createRefs()
                Box(
                    modifier = Modifier
                        .height((width * 280 / 375 - 30).dp)
                        .fillMaxWidth()
                        .background(
                            brush = Brush.linearGradient(
                                colorStops = colorStops,
                                start = Offset.Infinite.copy(x = 0f),
                                end = Offset.Infinite.copy(y = 0f)
                            )
                        )
                        .constrainAs(boxRef) {
                            top.linkTo(parent.top, 0.dp)
                            start.linkTo(parent.start, 0.dp)
                        }
                )

                Row(
                    modifier = Modifier.constrainAs(menuRef) {
                        top.linkTo(parent.top, 43.dp)
                        start.linkTo(parent.start, 22.dp)
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (getUserSession()?.role == 0) {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    if (drawerState.isOpen) {
                                        drawerState.close()
                                    } else {
                                        drawerState.open()
                                    }
                                }
                            },
                        ) {
                            Icon(
                                Icons.Outlined.Menu,
                                contentDescription = "menu",
                                tint = Color(0xFFFFFFFF),
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(14.dp))
                            )
                        }
                    }
                    Column(modifier = Modifier.padding(start = 8.dp)) {
                        Text(
                            text = "Vị trí",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFFB7B7B7),
                        )

                        Text(
                            text = stateMap.location,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFFFFFFFF),
                        )
                    }
                }



                SubcomposeAsyncImage(
                    model = getUserSession()?.avatar,
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .constrainAs(avatarRef) {
                            top.linkTo(parent.top, 43.dp)
                            end.linkTo(parent.end, 30.dp)
                        }
                        .clickable { changePage.invoke(MainActivity.Route.route_settings) }
                ) {
                    val statePainter = painter.state
                    if (statePainter is AsyncImagePainter.State.Loading || statePainter is AsyncImagePainter.State.Error) {
                        CircularProgressIndicator()
                    } else {
                        SubcomposeAsyncImageContent()
                    }
                }

                SearchView(
                    modifier = Modifier
                        .size(
                            width = (width - 60).dp,
                            height = 56.dp
                        )
                        .constrainAs(searchRef) {
                            bottom.linkTo(bannerRef.top, 24.dp)
                            start.linkTo(parent.start, 30.dp)
                            end.linkTo(parent.end, 30.dp)
                        }
                        .clickable {
                            changePage(MainActivity.Route.route_search)
                        }
                )

                Banner(
                    modifier = Modifier
                        .size(
                            width = (width - 60).dp,
                            height = ((width - 60) * 140 / 315).dp
                        )
                        .constrainAs(bannerRef) {
                            top.linkTo(boxRef.bottom, ((width - 60) * 140 / 315 / -2).dp)
                            start.linkTo(parent.start, 30.dp)
                        }
                )

                ChipBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(chipRef) {
                            top.linkTo(bannerRef.bottom, 0.dp)
                            start.linkTo(parent.start, 0.dp)
                        }
                        .padding(top = 20.dp, bottom = 20.dp),
                    chipState = chipState,
                    state = stateCoffee
                )
            }
        }
        items(listProduct.value.size) { index ->
            val item = listProduct.value[index]
            CoffeeItem(coffe = item, isRight = index % 2 == 0, onClick = {
                coffeeViewModel.setCoffeeSelected(item)
                changePage.invoke(MainActivity.Route.route_detail_coffe)
            }, addToCart = {
                coffeeViewModel.setCoffeeSelected(item)
                showBottomSheet.value = true
            })
        }
    }

    stateCoffee.coffeeSelected?.let { coffee ->
        BottomSheet(
            showBottomSheet = showBottomSheet, sheetState = sheetState, scope = scope, coffee
        )
    }
}


@Composable
fun ChipBar(modifier: Modifier, chipState: MutableState<String>, state: CoffeeStatusState) {
    val lazyChipsState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    LazyRow(
        state = lazyChipsState,
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 30.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(state.listCoffeeType.size) { index ->
            val item = state.listCoffeeType[index].name
            ElevatedAssistChip(
                onClick = {
                    coroutineScope.launch {
                        chipState.value = item
                        lazyChipsState.animateScrollToItem(index)
                    }
                },
                label = {
                    Text(
                        text = state.listCoffeeType[index].name,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                    )
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp)),
                colors = AssistChipDefaults.elevatedAssistChipColors(
                    containerColor = if (item == chipState.value) Color(0xFFC67C4E) else Color(
                        0xFFFFFFFF
                    ),
                    labelColor = if (item == chipState.value) Color(0xFFFFFFFF) else Color(
                        0xFF2F4B4E
                    )
                ),
            )
        }
    }

}


@Composable
fun SearchView(modifier: Modifier) {
    TextField(
        value = "Tìm kiếm loại cà phê", onValueChange = {}, enabled = false,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp)),
        colors = TextFieldDefaults.colors(
            disabledContainerColor = Color(0xFF989898),
            disabledTextColor = Color(0xFFEDEDED)
        ),
        leadingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.icon_search),
                contentDescription = "",
                tint = Color(0xFFFFFFFF)
            )
        },
        trailingIcon = {
            Image(
                painter = painterResource(R.drawable.icon_filler),
                contentDescription = "fillter",
                modifier = Modifier
                    .size(52.dp)
                    .padding(end = 8.dp)
            )
        },
        singleLine = true
    )
}

@Composable
fun Banner(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.banner_1), contentDescription = "banner",
        modifier = modifier
    )
}
