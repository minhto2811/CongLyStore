package com.mgok.conglystore.presentation.auth.user

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.google.firebase.auth.FirebaseAuth
import com.mgok.conglystore.R
import com.mgok.conglystore.component.MyElevatedButton
import com.mgok.conglystore.component.MyLoadingDialog
import com.mgok.conglystore.component.MyTextField
import com.mgok.conglystore.component.TrailingIconData
import com.mgok.conglystore.utils.ConvertMillisToDate
import com.mgok.conglystore.utils.isValidFullname
import com.mgok.conglystore.utils.isValidNumberphone
import com.mgok.conglystore.utils.removeNonAlphanumericVN
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateInfoUserScreen(
    userViewModel: UserViewModel = hiltViewModel(),
    onPopBackStack: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val user = FirebaseAuth.getInstance().currentUser

    val displayName = remember {
        mutableStateOf(removeNonAlphanumericVN(user?.displayName ?: ""))
    }

    val avatar = remember {
        mutableStateOf(user?.photoUrl ?: "")
    }

    val numberphone = remember {
        mutableStateOf(user?.phoneNumber ?: "")
    }

    val birthday = remember {
        mutableStateOf(ConvertMillisToDate(Calendar.getInstance().timeInMillis))
    }

    val gender = remember { mutableIntStateOf(0) }

    val visibleDialog = remember {
        mutableStateOf(false)
    }

    val visibleDatePickerDialog = remember {
        mutableStateOf(false)
    }


    val checkBoxState = remember {
        mutableStateOf(false)
    }


    val enableButton = remember {
        derivedStateOf {
            displayName.value.isValidFullname() && numberphone.value.isValidNumberphone()
                    && checkBoxState.value
        }
    }

    val datePickerState = rememberDatePickerState()

    val checkNumberphone = remember {
        mutableStateOf(false)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .focusRequester(focusRequester)
            .padding(28.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Cập nhật thông tin người dùng",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF1E1E1E),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.CenterHorizontally)
                .clip(CircleShape),
        ) {
            SubcomposeAsyncImage(
                model = avatar.value,
                contentDescription = "avatar",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            ) {
                val state = painter.state
                if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                    CircularProgressIndicator()
                } else {
                    SubcomposeAsyncImageContent()
                }
            }

            Box(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(color = Color(0x4D1e1f22))
                    .clickable {
                        //select image
                    }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.icon_camera),
                    contentDescription = "change avatar",
                    modifier = Modifier
                        .size(35.dp)
                        .align(Alignment.Center),
                    tint = Color(0xFFE1E1E1)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Họ và tên",
            style = MaterialTheme.typography.titleSmall,
            color = Color(0xFF2A2A2A)
        )
        Spacer(modifier = Modifier.height(8.dp))
        MyTextField(
            state = displayName,
            hint = "Nhập tên của bạn",
            keyboardType = KeyboardType.Text,
            hasSpace = true,
            maxChar = 40,
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            capitalization = KeyboardCapitalization.Words,

            )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Số điện thoại",
            style = MaterialTheme.typography.titleSmall,
            color = Color(0xFF2A2A2A)
        )
        Spacer(modifier = Modifier.height(8.dp))
        MyTextField(
            state = numberphone,
            hint = "Nhập số điện thoại",
            keyboardType = KeyboardType.Phone,
            maxChar = 10,
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            trailingIconData = TrailingIconData(
                icon = ImageVector.vectorResource(if (checkNumberphone.value) R.drawable.icon_check else R.drawable.icon_mail),
                onCLick = {
                    checkNumberphone.value = !checkNumberphone.value
                }
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Ngày sinh",
            style = MaterialTheme.typography.titleSmall,
            color = Color(0xFF2A2A2A)
        )
        Spacer(modifier = Modifier.height(8.dp))
        MyTextField(
            state = birthday,
            hint = "Ngày / tháng / năm",
            keyboardType = KeyboardType.Text,
            readOnly = true,
            maxChar = 10,
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            enableb = false,
            onClickable = {
                visibleDatePickerDialog.value = true
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Giới tính",
            style = MaterialTheme.typography.titleSmall,
            color = Color(0xFF2A2A2A)
        )
        Spacer(modifier = Modifier.height(8.dp))
        RadioGroupGender(gender)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Checkbox(
                checked = checkBoxState.value,
                onCheckedChange = { checkBoxState.value = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF648DDB)
                )
            )
            Text(
                text = "Xác nhận thông tin",
                style = MaterialTheme.typography.titleSmall,
                color = Color(0xFF2A2A2A)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        MyElevatedButton(
            title = "Tiếp tục", onClick = {
                focusManager.clearFocus()
                visibleDialog.value = true
            },
            enable = enableButton
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "Đăng nhập bằng tài khoản khác",
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFFB3B3B3),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    userViewModel.signOut()
                    onPopBackStack()
                }
        )

        DialogDatePicker(visibleDatePickerDialog, datePickerState, birthday)


        MyLoadingDialog(visible = visibleDialog)
    }
}


@Composable
fun RadioGroupGender(gender: MutableState<Int>) {
    val radioOptions = listOf("Nam", "Nữ", "Khác")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        radioOptions.forEachIndexed { index, item ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = (gender.value == index),
                    onClick = { gender.value = index },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFF648DDB)
                    )
                )
                Text(
                    text = item,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(end = 6.dp)
                )
                val icon = when (index) {
                    0 -> R.drawable.icon_gender_1
                    1 -> R.drawable.icon_gender_2
                    2 -> R.drawable.icon_gender_3
                    else -> R.drawable.icon_gender_1
                }
                Image(
                    imageVector = ImageVector.vectorResource(id = icon),
                    contentDescription = "icon gender",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogDatePicker(
    visibleDatePickerDialog: MutableState<Boolean>,
    datePickerState: DatePickerState,
    birthday: MutableState<String>
) {
    if (visibleDatePickerDialog.value) {
        DatePickerDialog(
            onDismissRequest = { visibleDatePickerDialog.value = false },
            confirmButton = {
                TextButton(onClick = {
                    visibleDatePickerDialog.value = false
                    birthday.value = ConvertMillisToDate(datePickerState.selectedDateMillis!!)
                }) {
                    Text(text = "Xác nhận")
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = Color.White
            )

        ) {
            DatePicker(state = datePickerState)
        }

    }
}

