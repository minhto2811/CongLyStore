package com.mgok.conglystore.presentation.coffee.manager_product

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.mgok.conglystore.component.MyElevatedButton
import com.mgok.conglystore.component.MyLoadingDialog
import com.mgok.conglystore.component.MyTextField
import com.mgok.conglystore.data.remote.coffee.Coffee
import com.mgok.conglystore.data.remote.coffee.Size

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCoffeeScreen(
    newCoffeeViewModel: NewCoffeeViewModel = hiltViewModel()
) {
    val stateUI by newCoffeeViewModel.stateUI.collectAsState()


    val type = remember {
        mutableStateOf("")
    }
    val focusRequester = remember {
        FocusRequester()
    }
    val focusManager = LocalFocusManager.current


    var expanded by remember {
        mutableStateOf(false)
    }
    val name = remember {
        mutableStateOf("")
    }

    LaunchedEffect(stateUI.listCoffeeType) {
        if (stateUI.listCoffeeType.isNotEmpty()) {
            name.value = stateUI.listCoffeeType[0].name
        }
    }

    var image by remember {
        mutableStateOf<Uri?>(null)
    }

    val listSize = remember {
        mutableStateListOf<Size>()
    }

    LaunchedEffect(stateUI.url) {
        image?.let { newCoffeeViewModel.deleteImage(it) }
        image = stateUI.url
    }

    val product = remember {
        derivedStateOf {
            Coffee(
                name = name.value,
                type = type.value,
                image = image.toString(),
                sizes = listSize
            )
        }
    }

    val enableButton = remember(listSize.size) {
        mutableStateOf(listSize.size > 0)
    }


    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                uri?.let {
                    image = it
                    newCoffeeViewModel.uploadImage(it, product.value.id)
                }
            })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .focusRequester(focusRequester)
            .padding(top = 100.dp, start = 30.dp, end = 30.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {
            expanded = !expanded
        }) {
            MyTextField(
                state = name,
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
                    if (option.name != name.value) {
                        DropdownMenuItem(text = {
                            Text(text = option.name)
                        }, onClick = {
                            focusManager.clearFocus()
                            name.value = option.name
                            expanded = !expanded
                        })
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        MyTextField(
            state = type,
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
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "add coffee",
                    tint = Color.White
                )
//                DropdownMenu(expanded = , onDismissRequest = { /*TODO*/ }) {
//
//                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow {

        }
        if (image != null) {
            SubcomposeAsyncImage(model = image,
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
                    newCoffeeViewModel.insertCoffee(product.value)
                    name.value = ""
                    type.value = ""
                    image = null
                }, enable = enableButton.value
            )
        } else {
            MyElevatedButton(
                title = "Chọn ảnh", onClick = {
                    galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }, enable = enableButton.value
            )
        }


        Spacer(modifier = Modifier.height(16.dp))


    }

    MyLoadingDialog(visible = stateUI.loading)
}



