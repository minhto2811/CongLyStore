package com.mgok.conglystore.presentation.home.tabs.tab_history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mgok.conglystore.MainActivity
import com.mgok.conglystore.R
import com.mgok.conglystore.component.LotifiesCompose
import com.mgok.conglystore.component.MyLoadingDialog
import com.mgok.conglystore.data.remote.bill.Bill
import com.mgok.conglystore.utilities.convertMillisToDate

@Composable
fun TabHistory(
    historyViewModel: HistoryViewModel = hiltViewModel(),
    changePage: (String) -> Unit
) {
    val stateUI by historyViewModel.stateUI.collectAsState()

    LaunchedEffect(Unit) {
        historyViewModel.getListBill()
    }

    MyLoadingDialog(visible = stateUI.loading)

    if (stateUI.listBill.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LotifiesCompose(
                resourceId = R.raw.history_animation,
                modifier = Modifier.size(250.dp)
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 56.dp),
        ) {
            items(items = stateUI.listBill, key = { it.id }) { bill ->
                BillItem(bill = bill, changePage = changePage)
            }
        }
    }
}


@Composable
fun BillItem(bill: Bill, changePage: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .padding(horizontal = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(
                        color = Color(0xFFC67C4E),
                        shape = CircleShape
                    )
            )
            Spacer(
                modifier = Modifier
                    .width(5.dp)
                    .fillMaxHeight()
                    .background(color = Color(0x48C67C4E))
            )
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, bottom = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val status =
                if (bill.paymentStatus == -1) "Thanh toán thất bại qua ví Momo" else if (bill.paymentStatus == 0) "Đã thanh toán quá ví Momo" else "Thanh toán khi nhận hàng"
            Text(text = convertMillisToDate(bill.date))
            Spacer(modifier = Modifier.height(4.dp))
            ElevatedCard(onClick = { changePage.invoke("${MainActivity.Route.route_bill_detail}/billId=${bill.id}") }) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Mã: ${bill.id}", style = MaterialTheme.typography.labelSmall)
                    Text(
                        text = "Tổng tiền: ${bill.price}",
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(text = "Trạng thái: $status", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

