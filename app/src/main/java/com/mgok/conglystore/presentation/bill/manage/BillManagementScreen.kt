package com.mgok.conglystore.presentation.bill.manage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mgok.conglystore.component.MyLoadingDialog
import com.mgok.conglystore.component.TopBar
import com.mgok.conglystore.data.remote.bill.Bill
import kotlinx.coroutines.launch

@Composable
fun BillManagementScreen(
    onPop: () -> Unit,
    viewModel: BillMangementViewModel = hiltViewModel(),
    onClickBill: (String) -> Unit
) {

    val stateUI by viewModel.stateUI.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.getListBill()
    }

    Scaffold(
        topBar = {
            TopBar(title = "Quản lý đơn hàng", onPop = onPop)
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                val state = rememberLazyListState()
                LazyRow(
                    state = state,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    val chip = mapOf(
                        -1 to "Chờ xác nhận",
                        0 to "Đang giao hàng",
                        1 to "Đã Giao hàng",
                        2 to "Hủy đơn"
                    )
                    items(chip.size) {index->
                        val it = chip.entries.elementAt(index)
                        val labelColor =
                            if (it.key == viewModel.status) Color.White else Color.Black
                        val containerColor =
                            if (it.key == viewModel.status) Color(0xFFc67c4e) else Color.Unspecified
                        ElevatedAssistChip(
                            onClick = {
                            viewModel.status = it.key
                            viewModel.getListBill()
                                scope.launch {
                                    state.animateScrollToItem(index,1)
                                }
                        }, label = {
                            Text(text = it.value,
                                modifier = Modifier.padding(vertical = 6.dp))
                        },
                            colors = AssistChipDefaults.elevatedAssistChipColors(
                                labelColor = labelColor,
                                containerColor = containerColor
                            ),

                        )
                    }
                }

            }
            items(items = stateUI.list, key = { it.id }) { bill ->
                BillItem(bill = bill) {
                    onClickBill.invoke(bill.id)
                }
            }
        }
        MyLoadingDialog(visible = stateUI.loading)
    }
}

@Composable
fun BillItem(bill: Bill, onClick: () -> Unit) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = bill.id)
        }
    }
}