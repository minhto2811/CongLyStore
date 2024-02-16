package com.mgok.conglystore.presentation.user

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.google.firebase.auth.FirebaseAuth
import com.mgok.conglystore.R
import com.mgok.conglystore.Session.setUserSession
import com.mgok.conglystore.component.MyElevatedButton
import com.mgok.conglystore.component.MyLoadingDialog
import com.mgok.conglystore.component.MyTextField
import com.mgok.conglystore.data.remote.user.User
import com.mgok.conglystore.presentation.auth.ResultStatusState
import com.mgok.conglystore.utilities.convertMillisToDate
import com.mgok.conglystore.utilities.NoRippleInteractionSource
import com.mgok.conglystore.utilities.isValidFullname
import com.mgok.conglystore.utilities.isValidNumberphone
import com.mgok.conglystore.utilities.removeNonAlphanumericVN
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateInfoUserScreen(
    userViewModel: UserViewModel = hiltViewModel(),
    onPopBackStack: () -> Unit,
    onHomeScreen: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val user = FirebaseAuth.getInstance().currentUser
    val state = userViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val displayName = remember {
        mutableStateOf(removeNonAlphanumericVN(user?.displayName ?: ""))
    }

    val avatar = remember {
        mutableStateOf(user?.photoUrl)
    }


    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                userViewModel.updateAvatar(it, user?.uid.toString())
            }
        }
    )


    val numberphone = remember {
        mutableStateOf(user?.phoneNumber ?: "")
    }

    val birthday = remember {
        mutableStateOf(convertMillisToDate(Calendar.getInstance().timeInMillis))
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
                    && checkBoxState.value && state.value.uploadState.status != ResultStatusState.Loading
        }
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Calendar.getInstance().timeInMillis
    )

    val newUser = remember {
        derivedStateOf {
            User(
                uid = user?.uid.toString(),
                displayName = displayName.value,
                avatar = avatar.value.toString(),
                birthday = datePickerState.selectedDateMillis!!,
                gender = gender.intValue,
                numberphone = numberphone.value,
                role = 1
            )
        }
    }

    LaunchedEffect(key1 = Unit) {
        user?.photoUrl?.let { uri ->
            userViewModel.updateAvatarWithLinkOther(uri.toString(), user.uid)
        }
    }

    LaunchedEffect(key1 = state.value.uploadState.status) {
        when (state.value.uploadState.status) {
            ResultStatusState.Error -> {
                Toast.makeText(context, state.value.uploadState.error, Toast.LENGTH_SHORT)
                    .show()
                userViewModel.resetState()
            }

            ResultStatusState.Successful -> {
                state.value.uploadState.url?.let { newUri ->
                    avatar.value = newUri
                    userViewModel.resetState()
                }
            }

            ResultStatusState.Default -> {

            }

            ResultStatusState.Loading -> {
                Log.e("ghg ", "okoikokok")
            }

        }
    }



    LaunchedEffect(key1 = state.value.status) {
        when (state.value.status) {
            ResultStatusState.Error -> {
                Toast.makeText(context, state.value.error, Toast.LENGTH_SHORT).show()
            }

            ResultStatusState.Successful -> {
                setUserSession(newUser.value)
                onHomeScreen()
            }

            ResultStatusState.Default -> {

            }

            ResultStatusState.Loading -> {

            }

        }
        userViewModel.resetState()
    }

    LaunchedEffect(key1 = state.value.error) {
        state.value.error?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
        userViewModel.resetState()
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
                .clip(CircleShape)
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color(0XFFE1E1E1)
                    ),
                    shape = CircleShape
                ),
        ) {
            SubcomposeAsyncImage(
                model = avatar.value,
                contentDescription = "avatar",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            ) {
                val stateAsync = painter.state
                if (stateAsync is AsyncImagePainter.State.Loading ||
                    state.value.uploadState.status == ResultStatusState.Loading
                ) {
                    CircularProgressIndicator()
                } else if (stateAsync is AsyncImagePainter.State.Error) {
                    if (avatar.value != null)
                        Toast.makeText(
                            context,
                            "Tải ảnh thất bại",
                            Toast.LENGTH_SHORT
                        ).show()
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
                        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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
            enableb = false,
            maxChar = 10,
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
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
                Log.e("ghg", newUser.value.toString())
                userViewModel.createInfoUser(newUser.value)
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
                .clickable(
                    indication = null,
                    interactionSource = NoRippleInteractionSource()
                ) {
                    userViewModel.signOut()
                    avatar.value?.let { userViewModel.deleteAvatar(it) }
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
                    birthday.value = convertMillisToDate(datePickerState.selectedDateMillis!!)
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

