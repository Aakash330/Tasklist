package com.tasklist.util

import android.content.Context
import android.widget.Toast
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String.StringLimit(limit: Int): String {
    if (this.length > limit) {
        return this.substring(0, limit) + " ..."
    } else {
        return this
    }
}

fun String.convertDatesFromat(inputPattern: String, outputPattern: String): String {
    val inputFormat = SimpleDateFormat(inputPattern)
    // inputFormat.timeZone= TimeZone.getTimeZone("IST")
    val outputFormat = SimpleDateFormat(outputPattern)
    val date: Date?
    var formattedValue = ""

    try {
        date = inputFormat.parse(this)
        formattedValue = outputFormat.format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return formattedValue
}

fun Context.toToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}