package com.example.app.utils

import com.example.app.models.ProdutoModel
import java.text.NumberFormat
import java.util.*

fun converDoubleToPrice (value: Double): String {
    val formatter = NumberFormat.getCurrencyInstance()
    formatter.currency = Currency.getInstance(Locale.getDefault())
    formatter.maximumFractionDigits = 2

    return formatter.format(value).replace(".", ",")
}

fun melhorPrecoProduto (produto: ProdutoModel): Double {
    return (if (produto.preco_promocional > 0F) produto.preco_promocional else produto.preco)
}
