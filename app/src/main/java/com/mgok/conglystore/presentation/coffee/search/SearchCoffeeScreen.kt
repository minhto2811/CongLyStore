package com.mgok.conglystore.presentation.coffee.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.twotone.Check
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mgok.conglystore.R
import com.mgok.conglystore.component.CoffeeItem
import com.mgok.conglystore.component.MyLoadingDialog
import com.mgok.conglystore.data.remote.coffee_type.CoffeeType
import com.mgok.conglystore.utilities.NoRippleInteractionSource

@Composable
fun SearchCoffeeScreen(
    searchViewModel: SearchViewModel = hiltViewModel(),
    changePage: (String) -> Unit,
    onPop: () -> Unit
) {
    val stateUI by searchViewModel.stateUI.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0x97E4DEDE)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        SearchBar(searchViewModel, onPop, stateUI.listCoffeeType) {
            searchViewModel.getListCoffeeByText()
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), contentPadding = PaddingValues(vertical = 10.dp)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                if (stateUI.listCoffee.isEmpty()) {
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.search_coffee_animation))
                    LottieAnimation(
                        composition = composition,
                        iterations = Int.MAX_VALUE,
                    )
                }
            }
            items(stateUI.listCoffee.size) { index ->
                val item = stateUI.listCoffee[index]
                CoffeeItem(coffe = item, isRight = index % 2 == 0, onClick = {
                    changePage.invoke("detail_coffee/${item.id}")
                }, addToCart = {})
            }
        }
    }
    MyLoadingDialog(visible = stateUI.loading)
}


@Composable
fun SearchBar(
    searchViewModel: SearchViewModel,
    onPop: () -> Unit,
    listCoffeeType: List<CoffeeType>,
    onSearch: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    var expand by remember {
        mutableStateOf(false)
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }



    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colorStops = arrayOf(
                        0f to Color(0xFF3A3737),
                        1f to Color(0xFF131313),
                    ), start = Offset.Infinite.copy(x = 0f), end = Offset.Infinite.copy(y = 0f)
                )
            )
            .padding(top = 40.dp, bottom = 20.dp, end = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            contentDescription = "back",
            tint = Color.White,
            modifier = Modifier
                .size(36.dp)
                .clickable { onPop.invoke() })


        TextField(
            value = searchViewModel.query.value,
            onValueChange = {
                val text = it.replace("  ", " ")
                searchViewModel.query.value = text
                searchViewModel.getListCoffeeByText()
            },
            placeholder = {
                Text(
                    text = "Tìm kiếm sản phẩm",
                )
            },
            leadingIcon = {
                Icon(
                    Icons.TwoTone.Search, contentDescription = null,
                )
            },
            trailingIcon = {
                Box(
                    modifier = Modifier.padding(horizontal = 8.dp),
                ) {
                    Image(painter = painterResource(R.drawable.icon_filler),
                        contentDescription = "fillter",
                        modifier = Modifier
                            .size(44.dp)
                            .clickable {
                                focusManager.clearFocus()
                                expand = !expand
                            })
                    DropdownMenu(
                        modifier = Modifier
                            .background(color = Color.White)
                            .padding(horizontal = 20.dp),
                        expanded = expand,
                        onDismissRequest = { expand = false },
                    ) {
                        listCoffeeType.forEach { option ->
                            val enable = searchViewModel.coffeeName.value != option.name
                            DropdownMenuItem(text = {
                                Text(
                                    text = option.name,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }, onClick = {
                                if (searchViewModel.coffeeName.value != option.name) {
                                    searchViewModel.coffeeName.value = option.name
                                    searchViewModel.getListCoffeeByText()
                                }
                                expand = false
                            }, colors = MenuDefaults.itemColors(
                                disabledTextColor = Color(0xFFC67C4E),
                            ), enabled = enable,
                                trailingIcon = {
                                    if (!enable) {
                                        Icon(Icons.TwoTone.Check, contentDescription = "")
                                    }
                                }
                            )
                        }

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                searchViewModel.coffeeName.value = ""
                                searchViewModel.getListCoffeeByText()
                                expand = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFC67C4E),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(3.dp)
                        ) {
                            Text(
                                text = "Làm mới",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(onSearch = {
                focusManager.clearFocus()
                onSearch()
            }),
            modifier = Modifier
                .focusRequester(focusRequester)
                .wrapContentHeight()
                .weight(1f)
                .background(color = Color.White, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp)),
            interactionSource = NoRippleInteractionSource(),

            )
    }
}

