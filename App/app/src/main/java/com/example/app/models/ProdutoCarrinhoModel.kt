package com.example.app.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity
data class ProdutoCarrinhoModel (
    @PrimaryKey
    var id_produto: String,

    @ColumnInfo(name = "quantidade", defaultValue = "1")
    var quantidade: Int? = 1
)
