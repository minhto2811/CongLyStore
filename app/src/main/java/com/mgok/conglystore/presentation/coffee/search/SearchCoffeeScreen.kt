package com.mgok.conglystore.presentation.coffee.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.mgok.conglystore.MainActivity
import com.mgok.conglystore.R
import com.mgok.conglystore.component.CoffeeItem
import com.mgok.conglystore.data.local.coffee.CoffeeType
import com.mgok.conglystore.data.remote.coffee.Coffee
import com.mgok.conglystore.presentation.coffee.CoffeeViewModel
import com.mgok.conglystore.utilities.NoRippleInteractionSource

@Composable
fun SearchCoffeeScreen(
    coffeeViewModel: CoffeeViewModel, changePage: (String) -> Unit, onPop: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0x97E4DEDE)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val state = coffeeViewModel.state.collectAsState()
        val data = remember {
            mutableStateListOf<Coffee>()
        }
        val query = rememberSaveable {
            mutableStateOf("")
        }
        val nameQuery = rememberSaveable {
            mutableStateOf("")
        }


        LaunchedEffect(key1 = query.value, key2 = nameQuery.value) {
            data.clear()
            if (query.value.isEmpty()) return@LaunchedEffect
            data.addAll(state.value.listCoffee.filter { coffee ->
                if (nameQuery.value.isEmpty()) {
                    coffee.name.lowercase().contains(query.value.lowercase()) ||
                            coffee.type.lowercase().contains(query.value.lowercase())
                } else {
                    coffee.name == nameQuery.value &&
                            (coffee.name.lowercase().contains(query.value.lowercase()) ||
                                    coffee.type.lowercase().contains(query.value.lowercase()))
                }
            })
        }
        SearchBar(query, onPop, state.value.listCoffeeType, nameQuery)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), contentPadding = PaddingValues(vertical = 10.dp)
        ) {
            items(data.size) { index ->
                val item = data[index]
                CoffeeItem(coffe = item, isRight = index % 2 == 0, onClick = {
                    coffeeViewModel.setCoffeeSelected(item)
                    changePage.invoke(MainActivity.Route.route_detail_coffe)
                }, addToCart = {})
            }
        }
    }
}


@Composable
fun SearchBar(
    query: MutableState<String>,
    onPop: () -> Unit,
    listCoffeeType: List<CoffeeType>,
    nameQuery: MutableState<String>
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val expand = remember {
        mutableStateOf(false)
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
            value = query.value,
            onValueChange = {
                val text = it.replace("  ", " ")
                query.value = text
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
                Box(modifier = Modifier.padding(horizontal = 8.dp)) {
                    Image(painter = painterResource(R.drawable.icon_filler),
                        contentDescription = "fillter",
                        modifier = Modifier
                            .size(44.dp)
                            .clickable {
                                focusManager.clearFocus()
                                expand.value = !expand.value
                            })
                    DropdownMenu(
                        expanded = expand.value,
                        onDismissRequest = { expand.value = false },
                    ) {
                        var selected by remember {
                            mutableStateOf(nameQuery.value)
                        }
                        listCoffeeType.forEach { option ->
                            val enable = selected != option.name
                            DropdownMenuItem(text = {
                                Text(
                                    text = option.name,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }, onClick = {
                                focusManager.clearFocus()
                                selected = option.name
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
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.padding(horizontal = 12.dp)
                        ) {
                            Button(
                                onClick = {
                                    selected = ""
                                    nameQuery.value = ""
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Red,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(3.dp)
                            ) {
                                Text(
                                    text = "Làm mới",
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                            Button(
                                onClick = {
                                    nameQuery.value = selected
                                    expand.value = false
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Green,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(3.dp),
                            ) {
                                Text(
                                    text = "Xác nhận",
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
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
                //
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

