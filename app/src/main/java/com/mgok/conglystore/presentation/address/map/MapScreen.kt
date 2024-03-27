package com.mgok.conglystore.presentation.address.map

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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
import com.mgok.conglystore.utilities.NoRippleInteractionSource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    addressDefault: String?,
    onPop: (String?) -> Unit,
    mapsViewModel: MapsViewModel = hiltViewModel()
) {
    val stateUI by mapsViewModel.stateUI.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(stateUI.error) {
        stateUI.error?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
    }

    LaunchedEffect(Unit) {
        if (addressDefault != null) {
            mapsViewModel.searchText = addressDefault
            mapsViewModel.getListLocationByName()
        } else {
            mapsViewModel.getLocation()
        }
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
                onPop.invoke(null)
            }) {
                Text(
                    text = "Xác nhận",
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = NoRippleInteractionSource()
                        ) {
                            mapsViewModel.visibleDialog.value = !mapsViewModel.visibleDialog.value
                        }
                        .padding(end = 20.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = if (stateUI.allow) Color(0xFFC67C4E) else Color(0x54C67C4E)
                )
            }
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
                },
                onMapLoaded = {
                    stateUI.location?.let { location ->
                        val latlng = LatLng(location.latitude, location.longitude)
                        markedState.apply {
                            position = latlng
                        }
                        mapsViewModel.getLocationByLatlng(latlng)
                        scope.launch {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(latlng, 16f), 500
                            )
                        }
                    }
                },
                onMyLocationClick = { location ->
                    val latlng = LatLng(location.latitude, location.longitude)
                    markedState.apply {
                        position = latlng
                    }
                    mapsViewModel.getLocationByLatlng(latlng)
                    scope.launch {
                        cameraPositionState.animate(
                            CameraUpdateFactory.newLatLngZoom(latlng, 16f), 500
                        )
                    }
                }
            ) {
                Marker(
                    state = markedState,
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                )
            }
        }
    }

    ShowDialog(mapsViewModel.visibleDialog, mapsViewModel.searchText) {
        onPop.invoke(mapsViewModel.searchText)
    }
}

@Composable
fun ShowDialog(visibleDialog: MutableState<Boolean>, searchText: String, onConfirm: () -> Unit) {
    if (visibleDialog.value) {
        Dialog(onDismissRequest = { visibleDialog.value = false }) {
            Column(
                modifier = Modifier
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(26.dp)
            ) {
                Text(
                    text = "Xác nhận địa chỉ:",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = searchText)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { visibleDialog.value = false },
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
                            visibleDialog.value = false
                            onConfirm()
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

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun Pre() {
    Text(
        text = "Xác nhận",
        modifier = Modifier.clickable(
            indication = null,
            interactionSource = NoRippleInteractionSource()
        ) {

        },
        style = MaterialTheme.typography.titleMedium,
        color = Color(0xFFC67C4E)
    )
}