package com.example.app.utils

import java.text.NumberFormat
import java.util.*

fun converDoubleToPrice (value: Double): String {
    val formatter = NumberFormat.getCurrencyInstance()
    formatter.currency = Currency.getInstance(Locale.getDefault())
    formatter.maximumFractionDigits = 2

    return formatter.format(value).replace(".", ",")
}