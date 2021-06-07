package com.example.app.models

data class ProdutoCompraModel (
    var id_produto: String,
    var quntidade: String
)

data class CompraModel (
    var id_usuario: String,
    var produtos: List<ProdutoCompraModel>
)
