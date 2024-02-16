package com.mgok.conglystore.utilities

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun convertMillisToDate(millis: Long): String {


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

inline fun <reified T> String.toListObject(): List<T> {
    val listType = object : TypeToken<List<T>>() {}.type
    return Gson().fromJson(this, listType)
}