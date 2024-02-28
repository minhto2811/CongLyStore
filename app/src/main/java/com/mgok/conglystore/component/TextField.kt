package com.mgok.conglystore.component

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.mgok.conglystore.R
import com.mgok.conglystore.utilities.NoRippleInteractionSource


@Composable
fun MyTextField(
    state: MutableState<String>,
    hint: String,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardActions: KeyboardActions,
    hasSpace: Boolean = false,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    readOnly: Boolean = false,
    maxChar: Int = 100,
    enableb: Boolean = true,
    onClickable: () -> Unit = {},
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    onValidate: (() -> Unit)? = null
) {
    val showPassword = remember {
        mutableStateOf(!isPassword)
    }
    OutlinedTextField(
        value = state.value,
        onValueChange = { text ->
            if (text.length <= maxChar) {
                state.value = if (hasSpace) text.replace("  ", " ") else text.trim()
            }
            onValidate?.invoke()
        },
        enabled = enableb,
        modifier = modifier
            .size(width = 376.dp, height = 56.dp)
            .clickable(
                indication = null,
                interactionSource = NoRippleInteractionSource()
            ) {
                onClickable()
            },
        textStyle = MaterialTheme.typography.labelSmall,
        placeholder = {
            Text(
                text = hint,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFFB3B3B3)
            )
        },
        singleLine = true,
        readOnly = readOnly,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            errorBorderColor = Color(0xFFEC6767),
            unfocusedBorderColor = Color(0xFFD9D9D9),
            focusedBorderColor = Color(0xFFC67C4E)
        ),
        keyboardOptions = KeyboardOptions(
            capitalization = capitalization,
            keyboardType = keyboardType,
            imeAction = ImeAction.Done,

            ),
        keyboardActions = keyboardActions,
        visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = { showPassword.value = !showPassword.value }) {
                    Icon(
                        imageVector = if (showPassword.value) ImageVector.vectorResource(R.drawable.icon_open_eye) else ImageVector.vectorResource(
                            R.drawable.icon_close_eye
                        ),
                        contentDescription = "trailing icon",
                    )
                }

            }
        },

        )
}