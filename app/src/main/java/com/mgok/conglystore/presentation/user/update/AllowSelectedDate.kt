package com.mgok.conglystore.presentation.user.update

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
class AllowSelectedDate: SelectableDates{
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis <= Calendar.getInstance().timeInMillis
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year <= Calendar.getInstance().get(Calendar.YEAR)
    }
}