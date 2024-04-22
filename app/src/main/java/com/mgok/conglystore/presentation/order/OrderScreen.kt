package com.mgok.conglystore.presentation.order

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.mgok.conglystore.component.ButtonPayment
import com.mgok.conglystore.component.MyLoadingDialog
import com.mgok.conglystore.component.Title
import com.mgok.conglystore.component.TopBar
import com.mgok.conglystore.data.remote.address.Address
import com.mgok.conglystore.data.remote.cart.Cart
import com.mgok.conglystore.data.remote.coffee.Coffee
import com.mgok.conglystore.utilities.openMomoPay
import java.util.UUID


@Composable
fun OrderScreen(
    addressId: String?,
    onPop: () -> Unit,
    orderViewModel: OrderViewModel = hiltViewModel(),
    onSelectAddress: (String) -> Unit,
    onOrder: () -> Unit
) {
    LaunchedEffect(Unit) {
        orderViewModel.getListCart()
        orderViewModel.getAddressById(addressId)
    }

    val context = LocalContext.current

    val stateUI by orderViewModel.stateUI.collectAsState()

    Scaffold(
        topBar = { TopBar(title = "Đặt hàng", onPop = onPop) }
    ) { paddingValue ->
        LazyColumn(
            modifier = Modifier.padding(
                top = paddingValue.calculateTopPadding(),
                start = 20.dp,
                end = 20.dp,
                bottom = paddingValue.calculateBottomPadding()
            )
        ) {
            item {
                Title(title = "Sản phẩm")
            }
            items(items = stateUI.listCart, key = { it.id }) { item: Cart ->
                ItemCart(coffee = item.coffee!!)
            }
            item {
                Title(title = "Địa chỉ")
                AdressSelected(address = stateUI.address) {
                    onSelectAddress.invoke(stateUI.address?.id ?: "1")
                }
            }
            val price: Long = (stateUI.totalPrice * 24000).toLong()
            item {
                Title(title = "Tổng tiền")
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()) {
                    Text(
                        text = "$price đồng",
                        modifier = Modifier.padding(vertical = 16.dp, horizontal = 22.dp)
                    )
                }
            }
            item {
                Title(title = "Thanh toán")
                ButtonPayment(
                    "Thanh toán qua ví Momo",
                    "https://img.mservice.io/momo-payment/icon/images/logo512.png"
                ) {
                    if (stateUI.address == null) {
                        Toast.makeText(context, "Địa chỉ trống", Toast.LENGTH_SHORT).show()
                        return@ButtonPayment
                    }
                    val billId = UUID.randomUUID().toString()
                    openMomoPay(context,billId,price)
                    orderViewModel.createBill(billId, price)
                }
                Spacer(modifier = Modifier.height(26.dp))

                ButtonPayment(
                    "Thanh toán khi nhận hàng",
                    "https://camautech.com/wp-content/uploads/2022/12/icon-thanh-toan.png"
                ) {
                    if (stateUI.address == null) {
                        Toast.makeText(context, "Địa chỉ trống", Toast.LENGTH_SHORT).show()
                        return@ButtonPayment
                    }
                    val billId = UUID.randomUUID().toString()
                    orderViewModel.createBill(billId, price, 1)
                    onOrder()
                }
                Spacer(modifier = Modifier.height(26.dp))
            }
        }

        MyLoadingDialog(visible = stateUI.loading)
    }
}


@Composable
fun AdressSelected(address: Address?, onSelectAddress: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        onClick = onSelectAddress
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            if (address == null) {
                Text(text = "Vui lòng chọn địa chỉ", style = MaterialTheme.typography.labelLarge)
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = address.displayName, style = MaterialTheme.typography.labelLarge)
                    Text(text = address.numberPhone, style = MaterialTheme.typography.labelLarge)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = address.location,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF2C2727)
                )
            }
        }
    }

}

@Composable
fun ItemCart(coffee: Coffee) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp)
            .padding(top = 6.dp)
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier
                .size(66.dp)
                .clip(shape = RoundedCornerShape(6.dp)),
            model = coffee.image,
            contentScale = ContentScale.Crop,
            loading = {
                CircularProgressIndicator()
            },
            contentDescription = "image"
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(start = 16.dp, end = 20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = coffee.name, style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = coffee.type, style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF725D5D)
            )
        }


    }
}


