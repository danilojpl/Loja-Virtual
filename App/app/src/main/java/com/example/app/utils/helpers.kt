package com.example.app.utils

import java.lang.StringBuilder
import java.text.NumberFormat
import java.util.*

fun converDoubleToPrice (value: Double): String {
    val formater = NumberFormat.getCurrencyInstance()
    formater.setMaximumFractionDigits(2)
    formater.setCurrency(Currency.getInstance("BRL"))

    return formater.format(value).replace(".", ",")
}