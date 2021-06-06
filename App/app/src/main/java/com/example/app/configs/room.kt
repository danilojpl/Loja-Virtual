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

fun setarQuantidade (context: Context, produto: ProdutoModel, quantidade: Int) {
    Thread {
        val carrinhoDB = buildCarrinhoDB(context)
        val produtosCarrinho = carrinhoDB.selecionar(produto.id)

        if (produtosCarrinho.isEmpty()) {
            carrinhoDB.inserir(ProdutoCarrinhoModel(id_produto = produto.id, quantidade = 1))
        } else {
            val produtoCarrinho = produtosCarrinho.first()
            carrinhoDB.atualizar_quantidade(produtoCarrinho.id_produto, quantidade)
        }


    }.start()
}

fun removerProduto (context: Context, produto: ProdutoCarrinhoModel) {
    Thread {
        val carrinhoDB = buildCarrinhoDB(context)
        carrinhoDB.inserir(produto)
    }
}