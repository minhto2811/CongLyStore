package com.mgok.conglystore.presentation.address.list_address

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.sharp.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mgok.conglystore.MainActivity
import com.mgok.conglystore.component.MyLoadingDialog
import com.mgok.conglystore.component.TopBar
import com.mgok.conglystore.data.remote.address.Address

@Composable
fun AddressScreen(
    addressViewModel: AddressViewModel = hiltViewModel(),
    onPop: () -> Unit,
    changePage: (String) -> Unit
) {
    val stateUI by addressViewModel.stateUI.collectAsState()

    LaunchedEffect(Unit) {
        addressViewModel.getListAddress()
    }


    MyLoadingDialog(visible = stateUI.loading)
    Scaffold(
        topBar = {
            TopBar(
                "Sổ địa chỉ",
                onPop,
                leading = {
                    IconButton(onClick = { changePage.invoke(MainActivity.Route.route_new_address) }) {
                        Icon(Icons.Filled.AddCircle, null)
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
            contentPadding = PaddingValues(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(
                items = stateUI.listAddress,
                key = { it.id }
            ) { address ->
                AddressItem(address = address, changePage = changePage)
            }
        }
    }
}


@Composable
fun AddressItem(address: Address, changePage: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Nguyễn Văn Nam",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "0321456987",
                style = MaterialTheme.typography.bodyMedium,
            )
            Box {
                var expand by remember {
                    mutableStateOf(false)
                }
                IconButton(onClick = { expand = true }) {
                    Icon(Icons.Sharp.MoreVert, "")
                }
                DropdownMenu(
                    expanded = expand,
                    onDismissRequest = { expand = false },
                    modifier = Modifier.background(color = Color.White)
                ) {
                    val list = listOf("Sửa", "Xóa")
                    list.forEach {
                        DropdownMenuItem(text = {
                            Text(text = it)
                        }, onClick = {
                            changePage.invoke("")
                            expand = false
                        })
                    }
                }
            }
        }
        Text(
            text = "108 P. Nguyễn Hoàng, Mỹ Đình, Nam Từ Liêm, Hà Nội, Vietnam",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
        )
    }
}

