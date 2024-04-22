package com.mgok.conglystore.presentation.bill.detail

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.mgok.conglystore.R
import com.mgok.conglystore.component.ButtonPayment
import com.mgok.conglystore.component.MyLoadingDialog
import com.mgok.conglystore.component.Title
import com.mgok.conglystore.component.TopBar
import com.mgok.conglystore.utilities.NoRippleInteractionSource
import com.mgok.conglystore.utilities.copyToClipboard
import com.mgok.conglystore.utilities.openMomoPay

@Composable
fun BillDetailScreen(
    isUpdatePaymentStatus: Boolean,
    billId: String?,
    refundId: String?,
    refundStatus: Int?,
    epId: String?,
    billViewModel: BillViewModel = hiltViewModel(),
    onPop: () -> Unit
) {
    val stateUI by billViewModel.stateUI.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = stateUI.bill, key2 = isUpdatePaymentStatus) {
        if (isUpdatePaymentStatus) {
            billId?.let { billViewModel.updatePaymentStatus(it) }
        } else {
            billId?.let { billViewModel.getBillById(it) }
        }
        billViewModel.checkRoleUser()
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    val dialog = remember {
        mutableStateOf<Int?>(null)
    }



    Scaffold(topBar = {
        TopBar(title = "Chi tiết hóa đơn", onPop = onPop, leading = {
            Box {
                if (stateUI.bill == null) return@Box
                val map = hashMapOf<Int, String>()
                if (stateUI.bill!!.status != 2 || stateUI.bill!!.status != 1) {
                    if (FirebaseAuth.getInstance().currentUser!!.uid == stateUI.bill!!.userId) {
                        if (stateUI.bill!!.paymentStatus == -1) map[0] = "Lỗi thanh toán"
                        if (stateUI.bill!!.status == -1) map[1] = "Xác nhận hủy đơn"
                    }
                    if (stateUI.role == 0) {
                        if (stateUI.bill!!.paymentStatus == -1) map[2] = "Xác nhận đã thanh toán"
                        if (map[1].isNullOrEmpty() && stateUI.bill!!.status < 1) map[1] =
                            "Xác nhận hủy đơn"
                        if (stateUI.bill!!.status == -1) map[5] = "Xác nhận đặt hàng thành công"
                        if (stateUI.bill!!.status == 0) map[6] = "Xác nhận giao hàng thành công"
                    }
                } else {
                    if (stateUI.role == 0 && refundStatus == -1) {
                        map[3] = "Hãy xác nhận hoàn tiền"
                        map[4] = "Từ chối hoàn tiền"
                    }
                }
                Log.e(
                    "BillDetailScreen: ",
                    "${stateUI.bill!!.status} - ${stateUI.role} - $refundStatus"
                )

                if (map.isNotEmpty()) {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert, contentDescription = "More"
                        )
                    }
                }

                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    map.forEach { (index, item) ->
                        DropdownMenuItem(text = { Text(item) }, onClick = {
                            expanded = false
                            dialog.value = index
                        })
                    }

                }
            }
        })
    }) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),
                start = 20.dp,
                end = 20.dp,
                bottom = paddingValues.calculateBottomPadding() + 26.dp
            ),
        ) {
            if (stateUI.bill == null) {
                item {
                    Text(text = "Không tìm thấy thông tin hóa đơn")
                }
            } else {
                item {
                    Title(title = "Mã hóa đơn")
                    Card {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stateUI.bill!!.id, style = MaterialTheme.typography.bodySmall
                            )
                            IconButton(onClick = {
                                copyToClipboard(
                                    context, "Mã đơn", stateUI.bill!!.id
                                )
                                Toast.makeText(
                                    context, "Đã sao chép mã đơn hàng", Toast.LENGTH_SHORT
                                ).show()
                            }) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.icon_copy),
                                    contentDescription = "copy"
                                )
                            }
                        }
                    }
                    Title(title = "Người nhận")
                    Card {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp)
                            ) {
                                Text(
                                    text = stateUI.bill!!.address.displayName,
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = stateUI.bill!!.address.numberPhone,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(start = 24.dp)
                                )
                            }
                            Text(
                                text = stateUI.bill!!.address.location,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0x5E000000)
                            )
                        }
                    }
                }
                item {
                    Title(title = "Danh sách sản phẩm")
                }

                items(count = stateUI.bill!!.list.size) { index ->
                    val item = stateUI.bill!!.list[index]
                    Card(modifier = Modifier.padding(top = 10.dp)) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SubcomposeAsyncImage(
                                model = item.image,
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(6.dp)),
                                loading = {
                                    CircularProgressIndicator()
                                },
                                contentScale = ContentScale.Crop,
                                contentDescription = "image"
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 16.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = TextUnit(
                                            14f, TextUnitType.Sp
                                        )
                                    ),
                                )
                                Text(
                                    text = item.type,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = TextUnit(
                                            12f, TextUnitType.Sp
                                        )
                                    ),
                                    color = Color(0x5E000000)
                                )
                                Text(
                                    text = "${item.quantity} x ${item.size}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = TextUnit(
                                            14f, TextUnitType.Sp
                                        )
                                    ),
                                    color = Color(0x5E000000)
                                )
                            }
                        }
                    }
                }

                item {
                    Title(title = "Thanh toán")
                    when (stateUI.bill!!.paymentStatus) {
                        -1 -> {
                            Text(
                                text = "Thanh toán thất bại qua ví Momo",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.Red
                                )
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            ButtonPayment(
                                title = "Thanh toán qua ví momo",
                                image = "https://img.mservice.io/momo-payment/icon/images/logo512.png",
                            ) {
                                openMomoPay(
                                    context, stateUI.bill!!.id, stateUI.bill!!.price, true
                                )
                            }
                        }

                        0 -> {
                            Text(
                                text = "Thanh toán bằng ví Momo",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.Green
                                )
                            )
                        }

                        else -> {
                            Text(
                                text = "Thanh toán khi nhận hàng",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.Green
                                )
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            ButtonPayment(
                                title = "Thanh toán qua ví momo",
                                image = "https://img.mservice.io/momo-payment/icon/images/logo512.png",
                            ) {
                                openMomoPay(
                                    context, stateUI.bill!!.id, stateUI.bill!!.price, true
                                )
                            }
                        }
                    }

                }
                item {
                    Title(title = "Trạng thái đơn hàng")
                    val text = when (stateUI.bill!!.status) {
                        -1 -> "Đang chuẩn bị"
                        0 -> "Đang giao hàng"
                        1 -> "Đã giao hàng"
                        else -> "Đã hủy đơn"
                    }
                    Text(
                        text = text, style = MaterialTheme.typography.bodySmall
                    )
                }

            }
        }
        MyLoadingDialog(visible = stateUI.loading)
        ShowDialogAction(
            role = stateUI.role,
            type = dialog,
            statusPm = stateUI.bill?.paymentStatus
        ) { value, percent ->
            when (value) {
                0 -> {
                    billViewModel.createErrorPayment(stateUI.bill!!.id)
                }

                1 -> {
                    billViewModel.cancelBill(
                        userId = stateUI.bill!!.userId,
                        billId = billId.toString(),
                        percent = percent
                    )
                }

                2 -> {
                    billViewModel.confirmPayment(epId.toString(), billId.toString(), 0, 0)
                }

                3 -> {
                    billViewModel.updateRefundStatus(refundId.toString(), 0, onPop)
                }

                4 -> {
                    billViewModel.updateRefundStatus(refundId.toString(), 1, onPop)
                }

                5 -> {
                    billViewModel.updateStatusBill(billId!!, 0)
                }

                6 -> {
                    billViewModel.updateStatusBill(billId!!, 1)
                }


                else -> {}
            }
        }
    }
}

@Composable
fun ShowDialogAction(
    role: Int,
    type: MutableState<Int?>,
    statusPm: Int?,
    onConfirm: (Int?, Float) -> Unit
) {
    if (type.value != null) {
        var title = ""
        var message = ""
        when (type.value) {
            0 -> {
                title = "Báo lỗi thanh toán"
                message = "Xác nhận báo lỗi bạn đã thanh toán đơn hàng này?"
            }

            1 -> {
                title = "Hủy đơn hàng"
                message = "Bạn chăc chắn muốn hủy đơn hàng này không?"
            }

            2 -> {
                title = "Xác nhận đơn hàng đã được thanh toán"
                message = "Bạn chăc chắn muốn xác nhận đơn hàng này đã được thanh toán không?"
            }

            3 -> {
                title = "Xác nhận hoàn tiền"
                message = "Bạn chăc chắn muốn xác nhận hoàn tiền cho đơn hàng này không?"
            }

            4 -> {
                title = "Từ chối hoàn tiền"
                message = "Bạn chăc chắn muốn từ chối hoàn tiền cho đơn hàng này không?"
            }

            5 -> {
                title = "Xác nhận đặt hàng thành công"
                message = "Bạn chăc chắn muốn xác nhận đặt hàng thành công không?"
            }

            6 -> {
                title = "Xác nhận giao hàng thành công"
                message = "Bạn chắc chắn muốn xác nhận giao hàng thành công không?"
            }

            else -> {}
        }
        Dialog(onDismissRequest = { type.value = null }) {
            Card(
                modifier = Modifier.wrapContentSize(), colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(26.dp)
                ) {
                    Text(
                        text = title, style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = message, style = MaterialTheme.typography.labelSmall
                    )
                    var percent by remember {
                        mutableFloatStateOf(if (statusPm == 0) 100f else 0f)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    if (type.value == 1 && statusPm == 0 && role == 0) {
                        var choose by remember {
                            mutableStateOf("Hoàn trả tiền")
                        }
                        val map = listOf(
                            "Hoàn trả tiền", "Không hoàn trả tiền", "Nhập phần trăm"
                        )
                        map.forEach { item ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(selected = choose == item, onClick = {
                                    choose = item
                                    percent = when (item) {
                                        "Hoàn trả tiền" -> 100f
                                        "Không hoàn trả tiền" -> 0f
                                        else -> 0f
                                    }
                                })
                                Text(text = item)
                            }
                        }
                        if (choose == "Nhập phần trăm") {
                            Spacer(modifier = Modifier.height(16.dp))
                            TextField(
                                value = percent.toString(), onValueChange = {
                                    percent = it.trim().toFloat()
                                }, singleLine = true, keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done, keyboardType = KeyboardType.Number
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Absolute.Right
                    ) {
                        Text(text = "Hủy", style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0x374D4848), fontSize = TextUnit(18f, TextUnitType.Sp)
                        ), modifier = Modifier.clickable(
                            indication = null, interactionSource = NoRippleInteractionSource()
                        ) {
                            type.value = null
                        })
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = "Xác nhận", style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = TextUnit(18f, TextUnitType.Sp)
                        ), modifier = Modifier.clickable(
                            indication = null, interactionSource = NoRippleInteractionSource()
                        ) {
                            onConfirm.invoke(type.value, percent)
                            type.value = null
                        })
                    }

                }
            }
        }
    }
}