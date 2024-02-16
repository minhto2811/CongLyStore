package com.mgok.conglystore.presentation.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mgok.conglystore.R
import com.mgok.conglystore.Session.getUserSession
import com.mgok.conglystore.component.TextButtonNormal
import com.mgok.conglystore.presentation.user.UserViewModel

@Composable
fun SettingsScreen(
    userViewModel: UserViewModel = hiltViewModel(),
    onSignOut:()->Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (getUserSession() != null) {
            AsyncImage(
                model = getUserSession()!!.avatar,
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = getUserSession()!!.displayName,
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.secondary)
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextButtonNormal(onClick = {
                userViewModel.signOut()
                onSignOut()
            }, title = stringResource(id = R.string.logout))
        }
    }
}