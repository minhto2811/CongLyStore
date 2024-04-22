package com.mgok.conglystore.utilities

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun convertMillisToDate(millis: Long): String {
    return try {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy kk:mm:ss", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        dateFormat.format(calendar.time)
    } catch (e: NumberFormatException) {
        "Ngày / tháng / năm"
    }
}

fun removeNonAlphanumericVN(input: String): String {
    return input.replace(Regex("[^a-zA-ZđĐàÀáÁảẢãÃạẠăĂằẰắẮẳẲẵẴặẶâÂầẦấẤẩẨẫẪậẬ"
            + "êÊềỀếẾểỂễỄệỆòÒóÓỏỎõÕọỌôÔồỒốỐổỔỗỖộỘơƠờỜớỚởỞỡỠợỢùÙúÚủỦũŨụỤưỪỪứỨửỬữỮựỰýÝỳỲỷỶỹỸỵỴ\\s]"),
        "").trim()
}

fun roundDownToMidnight(time: Long): Long {
    val calendar = Calendar.getInstance()
    calendar.time = Date(time)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.time.time
}

fun roundUpToMidnight(time: Long): Long {
    val calendar = Calendar.getInstance()
    calendar.time = Date(time)
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    return calendar.time.time
}

