package com.mgok.conglystore.presentation.address.map

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.mgok.conglystore.component.TopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onPop: () -> Unit,
    mapsViewModel: MapsViewModel = hiltViewModel()
) {
    val stateUI by mapsViewModel.stateUI.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(stateUI.error) {
        stateUI.error?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
    }

    val cameraPositionState = rememberCameraPositionState()
    val markedState = rememberMarkerState()

    val focusRequester = remember {
        FocusRequester()
    }

    val focusManager = LocalFocusManager.current

    val keyboardManager = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()


    Scaffold(
        topBar = {
            TopBar(title = "Bản đồ", onPop = {
                onPop.invoke()
            })
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding()),
        ) {
            Box {
                ExposedDropdownMenuBox(
                    expanded = stateUI.list.isNotEmpty(),
                    onExpandedChange = {
                        mapsViewModel.closeMenu()
                    },
                ) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White)
                            .focusRequester(focusRequester)
                            .menuAnchor(),
                        value = mapsViewModel.searchText,
                        onValueChange = { text ->
                            val newText = text.replace("  ", " ")
                            mapsViewModel.searchText = newText
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.White,
                            unfocusedIndicatorColor = Color.White
                        ),
                        placeholder = {
                            Text(text = "Tìm kiếm địa điểm", color = Color.Gray)
                        },
                        leadingIcon = {
                            Icon(Icons.Outlined.Search, contentDescription = null)
                        },
                        trailingIcon = {
                            if (stateUI.loading) {
                                CircularProgressIndicator()
                            } else
                                if (mapsViewModel.searchText.trim().isNotEmpty()) {
                                    IconButton(onClick = { mapsViewModel.searchText = "" }) {
                                        Icon(Icons.Outlined.Clear, contentDescription = null)
                                    }
                                }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            autoCorrect = true,
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                focusManager.clearFocus()
                                keyboardManager?.hide()
                                mapsViewModel.getListLocationByName()
                            }
                        ),
                    )
                    ExposedDropdownMenu(
                        expanded = stateUI.list.isNotEmpty(),
                        onDismissRequest = {}) {
                        stateUI.list.forEach { item ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = item.getAddressLine(0),
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                onClick = {
                                    val latLng = LatLng(item.latitude, item.longitude)
                                    markedState.apply {
                                        position = latLng
                                    }
                                    mapsViewModel.searchText = item.getAddressLine(0)
                                    mapsViewModel.closeMenu()
                                    focusManager.clearFocus()
                                    keyboardManager?.hide()
                                    scope.launch {
                                        cameraPositionState.animate(
                                            CameraUpdateFactory.newLatLngZoom(latLng, 16f), 500
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
            GoogleMap(
                modifier = Modifier
                    .weight(1f),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = true,
                    isTrafficEnabled = true,
                    minZoomPreference = 16f
                ),
                onMapClick = { latlng ->
                    markedState.apply {
                        position = latlng
                    }
                    mapsViewModel.getLocationByLatlng(latlng)
                },
                onPOIClick = { poi ->
                    markedState.apply {
                        position = poi.latLng
                    }
                    mapsViewModel.getLocationByLatlng(poi.latLng)
                }
            ) {
                Marker(
                    state = markedState,
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                )
            }
        }
    }
}