package com.mgok.conglystore.presentation.user.payment_error

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mgok.conglystore.component.MyLoadingDialog
import com.mgok.conglystore.component.TopBar
import com.mgok.conglystore.data.remote.error_payment.ErrorPayment

@Composable
fun PaymentErrorScreen(
    onPop: () -> Unit,
    viewModel: PaymentErrorViewModel = hiltViewModel(),
    onClick: (String,String) -> Unit
) {

    Scaffold(
        topBar = {
            TopBar(
                title = "Lỗi thanh toán",
                onPop = onPop
            )
        }
    ) { paddingValues ->
        val stateUI by viewModel.stateUI.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.getListPaymentError()
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding(),
                start = 20.dp, end = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        )
        {
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val chip = mapOf(
                        -1 to "Chờ xử lí",
                        0 to "Đã xử lí",
                        1 to "Đã từ chối"

                    )
                    items(items = chip.toList()) {
                        val labelColor =
                            if (it.first == viewModel.status) Color.White else Color.Black
                        val containerColor =
                            if (it.first == viewModel.status) Color(0xFFc67c4e) else Color.Unspecified
                        ElevatedAssistChip(onClick = {
                            viewModel.status = it.first
                            viewModel.getListPaymentError()
                        }, label = {
                            Text(text = it.second)
                        },
                            colors = AssistChipDefaults.elevatedAssistChipColors(
                                labelColor = labelColor,
                                containerColor = containerColor
                            ),
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }
                }
            }

            items(
                items = stateUI.list,
                key = { it.id }
            ) { item ->
                ErrorPaymentItem(item) {
                    onClick.invoke(item.id,item.billId)
                }
            }
        }

        MyLoadingDialog(visible = stateUI.loading)
    }
}

@Composable
fun ErrorPaymentItem(errorPayment: ErrorPayment, onClick: () -> Unit) {
    ElevatedCard(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = errorPayment.billId,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}