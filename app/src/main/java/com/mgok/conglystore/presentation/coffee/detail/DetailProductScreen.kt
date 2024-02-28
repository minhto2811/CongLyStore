package com.mgok.conglystore.presentation.coffee.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.material.icons.twotone.FavoriteBorder
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.mgok.conglystore.R
import com.mgok.conglystore.component.ListSizes
import com.mgok.conglystore.component.MyLoadingDialog
import com.mgok.conglystore.data.remote.coffee.Size
import com.mgok.conglystore.utilities.NoRippleInteractionSource


@Composable
fun DetailProductScreen(
    coffeeId: String,
    onPop: () -> Unit,
    detailCoffeeViewModel: DetailCoffeeViewModel = hiltViewModel()
) {

    val stateUI by detailCoffeeViewModel.stateUI.collectAsState()



    LaunchedEffect(Unit) {
        detailCoffeeViewModel.checkFavorite(coffeeId)
        detailCoffeeViewModel.getCoffeeById(coffeeId)
    }
    if (stateUI.coffee != null) {
        Scaffold(
            bottomBar = {
                BottomBar(stateUI.size) {
                    detailCoffeeViewModel.addCart()
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        start = 30.dp, end = 30.dp,
                        bottom = paddingValues.calculateBottomPadding(),
                        top = 20.dp
                    ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.icon_arrow_left),
                        contentDescription = "back",
                        tint = Color(0xFF2F2D2C),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(
                                indication = null,
                                interactionSource = NoRippleInteractionSource()
                            ) {
                                onPop()
                            }
                    )
                    Text(
                        text = "Chi tiết",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF2F2D2C)
                    )
                    Icon(
                        if (stateUI.isFavorite) Icons.TwoTone.Favorite else Icons.TwoTone.FavoriteBorder,
                        contentDescription = "heart",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(
                                indication = null,
                                interactionSource = NoRippleInteractionSource()
                            ) {
                                if (stateUI.isFavorite) {
                                    detailCoffeeViewModel.deleteFavorite()
                                } else {
                                    detailCoffeeViewModel.addFavorite()
                                }
                            },
                        tint = if (stateUI.isFavorite) Color(0xFFFFC0CB) else Color(0xFF2F2D2C)
                    )
                }


                SubcomposeAsyncImage(
                    model = stateUI.coffee?.image,
                    contentDescription = "image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(26.dp)),
                    contentScale = ContentScale.Crop
                ) {
                    val painterState = painter.state
                    if (painterState is AsyncImagePainter.State.Loading || painterState is AsyncImagePainter.State.Error) {
                        CircularProgressIndicator()
                    } else {
                        SubcomposeAsyncImageContent()
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))


                Text(
                    text = stateUI.coffee?.name.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF2F2D2C),
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stateUI.coffee?.type.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF9B9B9B),
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Star, contentDescription = "star",
                            tint = Color(0xFFFBBE21)
                        )
                        Text(
                            text = stateUI.coffee?.vote.toString(),
                            style = MaterialTheme.typography.labelLarge,
                            color = Color(0xFF2F2D2C),
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                        Text(
                            text = "(230)", style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFF808080)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(color = Color(0xFFF9F9F9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.bean),
                            contentDescription = ""
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(color = Color(0xFFF9F9F9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.milk),
                            contentDescription = ""
                        )
                    }


                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.5.dp)
                        .background(color = Color(0xFFE1E1E1))
                )
                Text(
                    text = "Mô tả", style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF2F2D2C),
                    modifier = Modifier.padding(vertical = 20.dp)
                )
                Text(
                    text = stateUI.coffee?.description.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF9B9B9B),
                )

                Text(
                    text = "Kích cỡ", style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF2F2D2C),
                    modifier = Modifier.padding(vertical = 20.dp)
                )

                ListSizes(stateUI.coffee?.sizes!!, stateUI.size?.size) {
                    detailCoffeeViewModel.updateSizeSelected(it)
                }



                Spacer(modifier = Modifier.height(20.dp))

            }
        }

    }
    MyLoadingDialog(visible = stateUI.loading)
}


@Composable
fun BottomBar(sizeState: Size?, addToCart: () -> Unit) {
    NavigationBar(
        tonalElevation = 10.dp,
        modifier = Modifier
            .wrapContentHeight()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .border(
                border = BorderStroke(1.dp, color = Color(0xFFF1F1F1)),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ),
        containerColor = Color(0xFFFFFFFF),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Giá",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF9B9B9B)
                )
                Text(
                    text = "\$ ${sizeState?.price}",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFFC67C4E),
                )
            }
            TextButton(
                onClick = {
                    addToCart.invoke()
                },
                modifier = Modifier
                    .height(56.dp)
                    .background(color = Color(0xFFC67C4E), shape = RoundedCornerShape(16.dp)),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color(0xFFC67C4E),
                    contentColor = Color(0xFFFFFFFF),
                ),
            ) {
                Text(
                    text = "Thêm vào giỏ hàng",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }
        }
    }
}

