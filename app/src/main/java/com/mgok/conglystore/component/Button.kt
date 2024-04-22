package com.mgok.conglystore.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.mgok.conglystore.utilities.NoRippleInteractionSource


@Composable
fun ButtonPayment(title:String, image:String, onclick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onclick
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubcomposeAsyncImage(
                modifier = Modifier.size(44.dp),
                model = image,
                loading = {
                    CircularProgressIndicator()
                },
                contentDescription = "momo"
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title)
        }
    }
}
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
fun MyElevatedButton(
    title: String,
    onClick: () -> Unit,
    enable: Boolean = true
) {
    ElevatedButton(
        onClick = onClick,
        modifier = Modifier.size(width = 376.dp, height = 56.dp),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = Color(0xFFC67C4E),
            disabledContainerColor = Color(0x66C67C4E)
        ),
        shape = RoundedCornerShape(12.dp),
        enabled = enable,
        interactionSource = NoRippleInteractionSource()
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

@Composable
fun MyLabelButton(icon: ImageVector, title: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(0.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 10.dp,
            pressedElevation = 20.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFC67C4E)
        ),
    ) {
        Icon(icon, "")
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp),
            textAlign = TextAlign.Start
        )
    }
}