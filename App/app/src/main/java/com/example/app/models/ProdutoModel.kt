package com.example.app.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProdutoModel (
    var atributos: List<ProdutoAtributoModel>,
    var categoria: String,
    var descricao: String,
    var estoque: Int,
    var id: String,
    var imagens: List<String>,
    var marca: String,
    var nome: String,
    var preco: Double,
    var preco_promocional: Double
) : Parcelable