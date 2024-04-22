package com.mgok.conglystore.presentation.revenue

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mgok.conglystore.component.DialogDatePicker
import com.mgok.conglystore.component.MyElevatedButton
import com.mgok.conglystore.component.MyLoadingDialog
import com.mgok.conglystore.component.MyTextField
import com.mgok.conglystore.component.Title
import com.mgok.conglystore.component.TopBar
import com.mgok.conglystore.presentation.user.update.AllowSelectedDate
import com.mgok.conglystore.utilities.convertMillisToDate
import com.mgok.conglystore.utilities.roundDownToMidnight
import com.mgok.conglystore.utilities.roundUpToMidnight
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RevenueScreen(
    onPop: () -> Unit,
    viewModel: RevenueViewModel = hiltViewModel()
) {
    val stateUI by viewModel.stateUI.collectAsState()
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Calendar.getInstance().timeInMillis,
        selectableDates = AllowSelectedDate()
    )
    var visibleDatePickerDialog by remember {
        mutableStateOf(false)
    }

    var dateStart by remember {
        mutableLongStateOf(roundDownToMidnight(Date().time))
    }

    var dateEnd by remember {
        mutableLongStateOf(roundUpToMidnight(Date().time))
    }

    val stringDateStart = remember(dateStart) {
        mutableStateOf(convertMillisToDate(dateStart).substring(0, 10))
    }
    val stringDateEnd = remember(dateEnd) {
        mutableStateOf(convertMillisToDate(dateEnd).substring(0, 10))
    }

    val context = LocalContext.current

    LaunchedEffect(stateUI.message) {
        stateUI.message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

    }
    Scaffold(
        topBar = { TopBar(title = "Doanh thu", onPop = onPop) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    start = 30.dp, end = 30.dp
                )
        ) {
            Title(title = "Ngày bắt đầu")
            MyTextField(
                state = stringDateStart,
                hint = "Ngày / tháng / năm",
                keyboardType = KeyboardType.Text,
                enableb = false,
                maxChar = 10,
                keyboardActions = KeyboardActions(),
                onClickable = {
                    visibleDatePickerDialog = true
                    viewModel.type = 0
                }
            )
            Title(title = "Ngày kết thúc")
            MyTextField(
                state = stringDateEnd,
                hint = "Ngày / tháng / năm",
                keyboardType = KeyboardType.Text,
                enableb = false,
                maxChar = 10,
                keyboardActions = KeyboardActions(),
                onClickable = {
                    visibleDatePickerDialog = true
                    viewModel.type = 1
                }
            )
            Spacer(modifier = Modifier.height(30.dp))
            MyElevatedButton(
                title = "Tra cứu", onClick = {
                    viewModel.getListBillByDate(dateStart, dateEnd)
                },
                enable = dateEnd >= dateStart
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(text = "Doanh thu: ${stateUI.data}vnđ")
            if (visibleDatePickerDialog) {
                DialogDatePicker(datePickerState) {
                    visibleDatePickerDialog = false
                    it?.let { time->
                        if (viewModel.type == 0) {
                            dateStart = roundDownToMidnight(time)
                        } else {
                            dateEnd = roundUpToMidnight(time)
                        }
                    }

                }
            }
        }
        MyLoadingDialog(visible = stateUI.loading)
    }

}