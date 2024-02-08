package com.mgok.conglystore.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun ConvertMillisToDate(millis: Long): String {
    return try {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
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