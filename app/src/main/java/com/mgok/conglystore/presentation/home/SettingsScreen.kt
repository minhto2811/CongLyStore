package com.mgok.conglystore.presentation.home

import android.util.Log
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
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.mgok.conglystore.R
import com.mgok.conglystore.component.TextButtonNormal

@Composable
fun SettingsScreen(onSignOut: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val userData = FirebaseAuth.getInstance().currentUser
        Log.e("ghg user","displayName "+ userData?.displayName.toString())
        Log.e("ghg user","uid "+ userData?.uid.toString())
        Log.e("ghg user","email "+  userData?.email.toString())
        Log.e("ghg user","phoneNumber "+  userData?.phoneNumber.toString())
        Log.e("ghg user","photoUrl "+  userData?.photoUrl.toString())
        if (userData?.photoUrl != null) {
            AsyncImage(
                model = userData.photoUrl,
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        if (userData?.displayName != null) {
            Text(
                text = userData.displayName!!,
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.secondary)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        TextButtonNormal(onClick = onSignOut, title = stringResource(id = R.string.logout))
    }
}