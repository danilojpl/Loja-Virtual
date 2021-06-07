package com.example.app.configs

import android.content.Context
import androidx.room.Room
import com.example.app.db.database
import com.example.app.db.ProdutoCarrinhoDAO
import com.example.app.models.ProdutoCarrinhoModel
import com.example.app.models.ProdutoModel

fun buildRoom (context : Context): database {
    return Room.databaseBuilder(context, database::class.java, "AppDB").build()
}

fun buildCarrinhoDB (context: Context): ProdutoCarrinhoDAO {
    return buildRoom(context).ProdutoCarrinhoDAO()
}

fun produtoExiste (context: Context, produto: ProdutoCarrinhoModel): ProdutoCarrinhoModel? {
    val carrinhoDB = buildCarrinhoDB(context)
    val produtosCarrinho = carrinhoDB.selecionar(produto.id_produto)

    if (produtosCarrinho.isEmpty()) {
        return null
    }

    return  produtosCarrinho.first()
}
