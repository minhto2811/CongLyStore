package com.mgok.conglystore.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.mgok.conglystore.data.remote.coffee.Coffee

@Composable
fun CoffeeItem(coffe: Coffee, isRight: Boolean, onClick: () -> Unit, addToCart: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(
                start = if (isRight) 8.dp else 4.dp,
                end = if (isRight) 4.dp else 8.dp,
                bottom = 8.dp
            )
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(16.dp))
                .background(color = Color(0xFFFFFFFF))
                .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 12.dp)
        ) {
            val (imageRef, nameRef, typeRef, priceRef, buttonRef) = createRefs()

            Box(modifier = Modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp))
                .constrainAs(imageRef) {
                    top.linkTo(parent.top, 0.dp)
                    start.linkTo(parent.start, 0.dp)
                }
                .clickable {
                    onClick()
                }) {
                SubcomposeAsyncImage(
                    model = coffe.image,
                    contentDescription = "image",
                    contentScale = ContentScale.Crop,
                ) {
                    val state = painter.state
                    if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                        CircularProgressIndicator()
                    } else {
                        SubcomposeAsyncImageContent()
                    }
                }
                Row(
                    modifier = Modifier
                        .background(
                            color = Color(0x26000000),
                            RoundedCornerShape(bottomEnd = 16.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 3.dp)
                ) {
                    Icon(
                        Icons.Filled.Star, contentDescription = null,
                        tint = Color(0xFFFBBE21),
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = coffe.vote.toString(), style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFFFFFFF)
                    )
                }
            }

            Text(
                text = coffe.name,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF2F2D2C),
                modifier = Modifier.constrainAs(nameRef) {
                    top.linkTo(imageRef.bottom, 12.dp)
                    start.linkTo(parent.start, 12.dp)
                }
            )

            Text(
                text = coffe.type,
                style = MaterialTheme.typography.titleSmall,
                color = Color(0xFF9B9B9B),
                modifier = Modifier.constrainAs(typeRef) {
                    top.linkTo(nameRef.bottom, 6.dp)
                    start.linkTo(parent.start, 12.dp)
                }
            )

            Text(
                text = "\$ ${coffe.sizes[0].price}",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF2F4B4E),
                modifier = Modifier.constrainAs(priceRef) {
                    top.linkTo(typeRef.bottom, 6.dp)
                    start.linkTo(parent.start, 12.dp)
                }
            )

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color = Color(0xFFC67C4E))
                    .constrainAs(buttonRef) {
                        top.linkTo(typeRef.bottom, 6.dp)
                        end.linkTo(parent.end, 12.dp)
                    }
                    .clickable {
                        addToCart.invoke()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.Add, contentDescription = "add", tint = Color(0xFFFFFFFF))
            }

        }
    }

}

