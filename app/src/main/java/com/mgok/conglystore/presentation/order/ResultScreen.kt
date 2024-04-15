package com.mgok.conglystore.presentation.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mgok.conglystore.R
import com.mgok.conglystore.component.LotifiesCompose
import kotlinx.coroutines.delay

@Composable
fun ResultScreen(
    orderId: String?,
    result: Int?,
    goHome: () -> Unit,
    resultViewModel: ResultViewModel = hiltViewModel()

) {
    var countDown by remember {
        mutableIntStateOf(10)
    }
    val raw = if (result == null || result == -1) R.raw.failed_animation
    else {
        resultViewModel.updateStatus(orderId!!, 0)
        R.raw.success_animation
    }


    LaunchedEffect(countDown) {
        while (countDown > 0) {
            delay(1000)
            countDown--
        }
        goHome()
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LotifiesCompose(resourceId = raw, modifier = Modifier.size(160.dp), iterations = 1)
        Spacer(modifier = Modifier.height(26.dp))
        ElevatedButton(onClick = goHome) {
            Text(text = "Tự động trở về sau ${countDown}s")
        }
    }
}