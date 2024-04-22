package com.mgok.conglystore.presentation.user.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.twotone.Refresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mgok.conglystore.MainActivity
import com.mgok.conglystore.R
import com.mgok.conglystore.component.MyLabelButton
import com.mgok.conglystore.component.TextButtonNormal

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    onSignOut: () -> Unit,
    changePage: (String) -> Unit
) {

    val stateUI by settingsViewModel.stateUI.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (stateUI.user != null) {
            AsyncImage(
                model = stateUI.user?.avatar,
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stateUI.user?.displayName.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.secondary)
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextButtonNormal(onClick = {
                settingsViewModel.signOut()
                onSignOut()
            }, title = stringResource(id = R.string.logout))
            MyLabelButton(icon = Icons.Default.LocationOn, title = "Sổ địa chỉ") {
                changePage.invoke(MainActivity.Route.route_address)
            }
            MyLabelButton(icon = Icons.Default.Info, title = "Báo lỗi thanh toán") {
                changePage.invoke(MainActivity.Route.route_payment_error)
            }

            MyLabelButton(icon = Icons.TwoTone.Refresh, title = "Yêu cầu hoàn hiền") {
                changePage.invoke(MainActivity.Route.route_refund)
            }
        }
    }
}

