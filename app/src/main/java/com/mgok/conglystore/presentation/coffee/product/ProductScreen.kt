package com.mgok.conglystore.presentation.coffee.product

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.mgok.conglystore.component.MyElevatedButton
import com.mgok.conglystore.component.MyLoadingDialog
import com.mgok.conglystore.component.MyTextField
import com.mgok.conglystore.data.remote.coffee.Coffee
import com.mgok.conglystore.presentation.auth.ResultStatusState
import com.mgok.conglystore.presentation.coffee.CoffeeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeeScreen(
    coffeeViewModel: CoffeeViewModel,
) {
    val state by coffeeViewModel.state.collectAsState()


    val type = remember {
        mutableStateOf("")
    }
    val focusRequester = remember {
        FocusRequester()
    }
    val focusManager = LocalFocusManager.current

    val enableButton = remember {
        derivedStateOf {
            true
        }
    }


    val visibleLoading = remember {
        mutableStateOf(false)
    }

    var expanded by remember {
        mutableStateOf(false)
    }
    val name = remember {
        mutableStateOf(state.listCoffeeType[0].name)
    }

    var image = remember {
        mutableStateOf<Uri?>(null)
    }

    val product = remember {
        derivedStateOf {
            Coffee(
                name = name.value, type = type.value, image = image.value.toString()
            )
        }
    }

    val context = LocalContext.current


    LaunchedEffect(key1 = state.status) {
        when (state.status) {
            ResultStatusState.Loading -> {
                visibleLoading.value = true
            }

            ResultStatusState.Successful -> {
                visibleLoading.value = false
                Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show()
                image.value = null
                type.value = ""
            }

            ResultStatusState.Default -> {
                visibleLoading.value = false
            }

            ResultStatusState.Error -> {
                visibleLoading.value = false
            }

        }
    }

    LaunchedEffect(key1 = state.uploadState.status) {
        when (state.uploadState.status) {
            ResultStatusState.Loading -> {
                visibleLoading.value = true
            }

            ResultStatusState.Successful -> {
                image.value?.let { coffeeViewModel.deleteImage(it) }
                visibleLoading.value = false
                image.value = state.uploadState.url
            }

            ResultStatusState.Default -> {
                visibleLoading.value = false
            }

            ResultStatusState.Error -> {
                visibleLoading.value = false
            }

        }
    }


    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                uri?.let {
                    image.value = it
                    coffeeViewModel.uploadImage(it, product.value.id.toString())
                }
            })

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .focusRequester(focusRequester)
            .padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
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
                    state.listCoffeeType.forEach { option ->
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

            LazyRow {

            }
            if (image.value != null) {
                SubcomposeAsyncImage(model = image.value,
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
                        coffeeViewModel.insertCoffee(product.value)
                    }, enable = enableButton
                )
            } else {
                MyElevatedButton(
                    title = "Chọn ảnh", onClick = {
                        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }, enable = enableButton
                )
            }


            Spacer(modifier = Modifier.height(16.dp))

        }

    }

    MyLoadingDialog(visible = visibleLoading)
}


@Composable
fun SizeItem(default: Boolean = false) {
    Box(modifier = Modifier
        .size(width = 56.dp, height = 56.dp)
        .clip(RoundedCornerShape(6.dp))
        .clickable {

        }) {

    }
}

