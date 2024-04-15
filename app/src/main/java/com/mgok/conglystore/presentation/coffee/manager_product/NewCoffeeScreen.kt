package com.mgok.conglystore.presentation.coffee.manager_product

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.mgok.conglystore.component.MyElevatedButton
import com.mgok.conglystore.component.MyLoadingDialog
import com.mgok.conglystore.component.MyTextField
import com.mgok.conglystore.component.TopBar

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NewCoffeeScreen(
    newCoffeeViewModel: NewCoffeeViewModel = hiltViewModel(),
    onPop: () -> Unit
) {
    val stateUI by newCoffeeViewModel.stateUI.collectAsState()

    val context = LocalContext.current

    val focusRequester = remember {
        FocusRequester()
    }
    val focusManager = LocalFocusManager.current


    var expanded by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(stateUI.listCoffeeType) {
        if (stateUI.listCoffeeType.isNotEmpty()) {
            newCoffeeViewModel.nameCoffee.value = stateUI.listCoffeeType[0].name
        }
    }

    LaunchedEffect(stateUI.error) {
        stateUI.error?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
    }



    LaunchedEffect(stateUI.url) {
        stateUI.url?.let { uri ->
            newCoffeeViewModel.imageCoffee?.let { newCoffeeViewModel.deleteImage() }
            newCoffeeViewModel.imageCoffee = uri.toString()
        }
    }


    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                uri?.let {
                    newCoffeeViewModel.uploadImage(it, newCoffeeViewModel.coffee.value.id)
                }
            })
    Scaffold(
        topBar = {
            TopBar("Thêm sản phẩm", onPop)
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester)
                .padding(top = paddingValues.calculateTopPadding(), start = 30.dp, end = 30.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {
                expanded = !expanded
            }) {
                MyTextField(
                    state = newCoffeeViewModel.nameCoffee,
                    hint = "Hãy thêm loại cà phê trước",
                    modifier = Modifier.menuAnchor(),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                    }),
                    readOnly = true
                )
                ExposedDropdownMenu(expanded = expanded,
                    onDismissRequest = { expanded = !expanded }) {
                    stateUI.listCoffeeType.forEach { option ->
                        if (option.name != newCoffeeViewModel.nameCoffee.value) {
                            DropdownMenuItem(text = {
                                Text(text = option.name)
                            }, onClick = {
                                focusManager.clearFocus()
                                newCoffeeViewModel.nameCoffee.value = option.name
                                expanded = !expanded
                            })
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            MyTextField(
                state = newCoffeeViewModel.typeCoffee,
                hint = "Nhập thành phần pha chế",
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
                capitalization = KeyboardCapitalization.Words,
                hasSpace = true
            )

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.padding(horizontal = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Kích thước (*)",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .weight(1f)
                )
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(
                            color = Color(0xFFC67C4E),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .clickable {
                            newCoffeeViewModel.showDialog.value = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "add coffee",
                        tint = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    count = newCoffeeViewModel.sizes.size,
                ) {
                    val item = newCoffeeViewModel.sizes[it]
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(color = Color(0xFFc67c4e), shape = RoundedCornerShape(8.dp))
                            .combinedClickable(
                                onClick = {
                                    Toast
                                        .makeText(context, "Giữ để xóa", Toast.LENGTH_SHORT)
                                        .show()
                                },
                                onLongClick = {
                                    newCoffeeViewModel.deleteItemSize(item.size)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = item.size, color = Color.White)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (newCoffeeViewModel.imageCoffee != null) {
                SubcomposeAsyncImage(model = newCoffeeViewModel.imageCoffee,
                    contentDescription = "image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(width = 376.dp, height = 200.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }) {
                    val painterState = painter.state
                    if (painterState is AsyncImagePainter.State.Loading || painterState is AsyncImagePainter.State.Error) {
                        CircularProgressIndicator()
                    } else {
                        SubcomposeAsyncImageContent()
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                MyElevatedButton(
                    title = "Thêm mới", onClick = {
                        focusManager.clearFocus()
                        newCoffeeViewModel.insertCoffee()
                    }, enable = newCoffeeViewModel.enableButton.value
                )
            } else {
                MyElevatedButton(
                    title = "Chọn ảnh", onClick = {
                        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


        }

        MyLoadingDialog(visible = stateUI.loading)
        DialogAddSize(
            visible = newCoffeeViewModel.showDialog,
            size = newCoffeeViewModel.size,
            price = newCoffeeViewModel.price,
            focusManager = focusManager,
            focusRequester = focusRequester
        ) {
            newCoffeeViewModel.addSize()
        }
    }
}

@Composable
fun DialogAddSize(
    visible: MutableState<Boolean>,
    size: MutableState<String>,
    price: MutableState<String>,
    focusRequester: FocusRequester,
    focusManager: FocusManager,
    callback: () -> Unit
) {
    if (visible.value) {
        Dialog(onDismissRequest = { visible.value = false }) {
            Column(
                modifier = Modifier
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
                    .focusRequester(focusRequester)
            ) {
                MyTextField(state = size,
                    maxChar = 20,
                    hint = "Nhập kích thước",
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ))
                Spacer(modifier = Modifier.height(12.dp))
                MyTextField(state = price, hint = "Nhập giá", keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                    hasSpace = false,
                    maxChar = 15,
                    keyboardType = KeyboardType.Number)
                Spacer(modifier = Modifier.height(12.dp))
                MyElevatedButton(title = "Xác nhận", onClick = {
                    visible.value = false
                    callback()
                })
            }
        }
    }
}


