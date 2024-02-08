package com.mgok.conglystore.utils

import android.text.TextUtils
import android.util.Patterns

fun String.isValidEmail() =
    !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPassword() =
    this.length > 5

fun String.isValidFullname(): Boolean {
    val minLength = 2
    val maxLength = 40

    if (this.length !in minLength..maxLength) {
        return false
    }

    val specialCharacters = "~`!@#$%^&*()-_=+\\|[{]};:'\",<.>/?"
    if (this.any { it in specialCharacters }) {
        return false
    }

    if (this.trim() != this) {
        return false
    }

    return this.all { it.isLetter() || it.isWhitespace() }
}

fun String.isValidNumberphone(): Boolean {

    if (this.length != 10) {
        return false
    }

    if (!this.all { it.isDigit() }) {
        return false
    }

    val specialCharacters = "~`!@#$%^&*()-_=+\\|[{]};:'\",<.>/?"

    return !this.any { it in specialCharacters }
}

fun String.isValidBirthday() = this.trim().isNotEmpty()

//fun String.isValidBirthday(): Boolean {
//    try {
//        val calendar = Calendar.getInstance()
//        val year = calendar.get(Calendar.YEAR)
//        val month = calendar.get(Calendar.MONTH)
//        val day = calendar.get(Calendar.DAY_OF_MONTH)
//        if (year < this.substring(7, 4).toInt()) {
//            return false
//        } else if (month < this.substring(4, 2).toInt()) {
//            return false
//        } else if (day < this.substring(0, 2).toInt()) {
//            return false
//        }
//        val regex = """^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/((19|20)\d\d)$""".toRegex()
//        return regex.matches(this)
//    } catch (e: NumberFormatException) {
//        return false
//    }catch (e:StringIndexOutOfBoundsException){
//        return false
//    }
//}