package com.example.demo.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

// Function to format last login date
fun formatDate(lastLoginDate: String): String {
    // Define the input format (ISO 8601)
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    inputFormat.timeZone = TimeZone.getTimeZone("UTC") // Set UTC timezone if the API gives a UTC date

    // Parse the string to Date
    val date: Date? = inputFormat.parse(lastLoginDate)

    // Define the output format (human-readable format)
    val outputFormat = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.getDefault())

    // Return the formatted date as a string
    return if (date != null) {
        outputFormat.format(date)
    } else {
        "Invalid date"
    }
}