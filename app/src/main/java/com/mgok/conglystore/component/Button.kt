package com.mgok.conglystore.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun TextButtonNormal(onClick: () -> Unit, title: String) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(vertical = 16.dp, horizontal = 32.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.primary)
        )
    }
}


@Composable
fun MyElevatedButton(title: String, onClick: () -> Unit, enable: State<Boolean>) {
    ElevatedButton(
        onClick = onClick,
        modifier = Modifier.size(width = 376.dp, height = 56.dp),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = Color(0xFF648DDB),
            disabledContainerColor = Color(0x66648DDB)
        ),
        shape = RoundedCornerShape(12.dp),
        enabled = enable.value
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFFFFFFF)
        )
    }
}

@Composable
fun MyOutlineButton(
    onClick: () -> Unit,
    @DrawableRes idLogoResource: Int,
    title: String,
    contentDescription: String = ""
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.size(width = 376.dp, height = 56.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFE1E1E1)
        ),
    ) {
        Image(
            painter = painterResource(
                id = idLogoResource
            ),
            contentDescription = contentDescription,
            modifier = Modifier
                .size(24.dp)
                .background(Color.Transparent)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = Color(0xFF2A2A2A)
        )
    }
}