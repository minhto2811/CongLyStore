package com.mgok.conglystore.presentation.address.list_address

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.sharp.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.mgok.conglystore.MainActivity
import com.mgok.conglystore.component.MyLoadingDialog
import com.mgok.conglystore.component.TopBar
import com.mgok.conglystore.data.remote.address.Address

@Composable
fun AddressScreen(
    addressSelectedId: String? = null,
    addressViewModel: AddressViewModel = hiltViewModel(),
    onPop: () -> Unit,
    changePage: (String, String?) -> Unit,
    onSelectedAddress: (String) -> Unit
) {
    val stateUI by addressViewModel.stateUI.collectAsState()

    LaunchedEffect(Unit) {
        addressViewModel.getListAddress()
    }

    val context = LocalContext.current


    MyLoadingDialog(visible = stateUI.loading)
    Scaffold(
        topBar = {
            TopBar(
                "Sổ địa chỉ",
                onPop,
                leading = {
                    IconButton(onClick = {
                        changePage.invoke(
                            MainActivity.Route.route_new_address,
                            null
                        )
                    }) {
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
                val selected = if (addressSelectedId == null) null else
                    address.id == addressSelectedId || stateUI.listAddress.size == 1
                AddressItem(
                    selected = selected,
                    address = address,
                    changePage = changePage,
                    deleteDialog = {
                        if (stateUI.listAddress.size == 1) {
                            Toast.makeText(
                                context,
                                "Phải có ít nhất một địa chỉ",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@AddressItem
                        }
                        addressViewModel.addressIdDel = address.id
                    }
                ) {
                    if (addressSelectedId != address.id) {
                        onSelectedAddress.invoke(address.id)
                    }
                }
            }
        }
        if (addressViewModel.addressIdDel != null) {
            Dialog(onDismissRequest = { addressViewModel.addressIdDel = null }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White, shape = RoundedCornerShape(6.dp))
                        .padding(26.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Xác nhận xóa địa chỉ")
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { addressViewModel.addressIdDel = null },
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0x99A29A95)
                            )
                        ) {
                            Text(
                                text = "Quay lại",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                        Button(
                            onClick = {
                                addressViewModel.deleteAddress(addressViewModel.addressIdDel!!)
                                addressViewModel.addressIdDel = null
                            },
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFC67C4E)
                            )
                        ) {
                            Text(
                                text = "Xác nhận",
                                style = MaterialTheme.typography.labelMedium
                            )

                        }
                    }
                }
            }
        }
    }
}


@Composable
fun AddressItem(
    selected: Boolean?,
    address: Address,
    changePage: (String, String?) -> Unit,
    deleteDialog: () -> Unit,
    onSelected: () -> Unit
) {
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
            selected?.let { RadioButton(selected = it, onClick = onSelected) }
            Text(
                text = address.displayName,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = address.numberPhone,
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
                            if (it == "Sửa") changePage.invoke(
                                MainActivity.Route.route_new_address,
                                address.id
                            ) else deleteDialog()
                            expand = false
                        })
                    }
                }
            }
        }
        Text(
            text = address.location,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
        )
    }
}

