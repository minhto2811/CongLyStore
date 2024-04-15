package com.mgok.conglystore.presentation.home.tabs.tab_cart

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.mgok.conglystore.MainActivity
import com.mgok.conglystore.R
import com.mgok.conglystore.component.BackgroundDelete
import com.mgok.conglystore.component.ListSizes
import com.mgok.conglystore.component.LotifiesCompose
import com.mgok.conglystore.component.MyLoadingDialog
import com.mgok.conglystore.data.remote.cart.Cart
import com.mgok.conglystore.data.remote.coffee.Size

@Composable
fun TabCart(
    cartViewModel: CartViewModel = hiltViewModel(),
    changePage: (String) -> Unit,
) {

    LaunchedEffect(Unit) {
        cartViewModel.getListCart()
    }
    val stateUI by cartViewModel.stateUI.collectAsState()


    MyLoadingDialog(visible = stateUI.loading)


    if (stateUI.listCart.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LotifiesCompose(
                resourceId = R.raw.cart_animation,
                modifier = Modifier.size(250.dp)
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp),
        ) {
            item {

                AnimatedVisibility(
                    visible = stateUI.totalPrice > 0f,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut(),
                ) {
                    Row(
                        modifier = Modifier
                            .height(100.dp)
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, top = 30.dp, bottom = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "\$${stateUI.totalPrice}")
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(color = Color(0xFFC67C4E))
                                .clickable {
                                    changePage.invoke(MainActivity.Route.rout_order)
                                }
                                .padding(horizontal = 20.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "Xác nhận",
                                color = Color.White
                            )
                        }
                    }
                }
            }
            items(
                items = stateUI.listCart,
                key = { it.id }
            ) { cart ->
                CartItem(
                    cart = cart,
                    onRemove = {
                        cartViewModel.deleteCart(cart.id)
                    },
                    gotoDetail = {
                        changePage.invoke("detail_coffee/${cart.idCoffee}")
                    },
                    onUpdate = { quan, sizeNew ->
                        with(cart) {
                            quantity = quan
                            size = sizeNew
                        }
                        cartViewModel.upsertCart(cart)
                    },
                )

                VerticalDivider(
                    modifier = Modifier.height(1.dp),
                    color = Color.Gray
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartItem(
    cart: Cart,
    onRemove: () -> Unit,
    gotoDetail: () -> Unit,
    onUpdate: (Int, String) -> Unit,
) {
    val width = LocalConfiguration.current.screenWidthDp

    val sizeState = remember {
        mutableStateOf(Size())
    }

    var quanState by remember {
        mutableIntStateOf(cart.quantity)
    }


    LaunchedEffect(Unit) {
        cart.coffee!!.sizes.forEach {
            if (it.size == cart.size) {
                sizeState.value = it
            }
        }
    }


    val state = rememberSwipeToDismissBoxState(
        positionalThreshold = with(LocalDensity.current) {
            { (width * 0.8).dp.toPx() }
        },
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onRemove()
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
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(vertical = 8.dp, horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubcomposeAsyncImage(
                model = cart.coffee!!.image,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        gotoDetail.invoke()
                    },
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
                    .padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = cart.coffee?.name.toString(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "\$${sizeState.value.price}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Text(
                    text = cart.coffee?.type.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF9B9B9B)
                )

                ListSizes(
                    sizes = cart.coffee!!.sizes,
                    sizeDefault = sizeState.value.size,
                    scale = 0.6f,
                ) { size ->
                    sizeState.value = size
                    onUpdate.invoke(quanState, size.size)
                }
            }

            Row {
                Box(modifier = Modifier
                    .background(
                        color = Color(0xFFC67C4E),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clickable {
                        --quanState
                        if (quanState >= 1)
                            onUpdate.invoke(quanState, sizeState.value.size)
                        else
                            onRemove()
                    }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.icon_remove),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                Text(
                    text = quanState.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                Box(modifier = Modifier
                    .background(
                        color = Color(0xFFC67C4E),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clickable {
                        ++quanState
                        onUpdate.invoke(quanState, sizeState.value.size)
                    }) {
                    Icon(
                        Icons.Default.Add, contentDescription = null,
                        tint = Color.White
                    )
                }
            }

        }

    }

}


