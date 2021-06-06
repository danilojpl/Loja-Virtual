package com.example.app.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProdutoAtributoModel (
    var chave: String,
    var valor: String
) : Parcelable
