package com.example.app.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.app.models.ProdutoCarrinhoModel

@Dao
interface ProdutoCarrinhoDAO {
    @Query("Select * FROM ProdutoCarrinhoModel")
    fun listarTodos(): List<ProdutoCarrinhoModel>

    @Query("UPDATE ProdutoCarrinhoModel SET quantidade = :quantidade WHERE id_produto = :id_produto")
    fun atualizar_quantidade(id_produto: String, quantidade: Int)

    @Query("SELECT * FROM ProdutoCarrinhoModel WHERE id_produto = :id_produto LIMIT 1")
    fun selecionar(id_produto: String): List<ProdutoCarrinhoModel>

    @Insert
    fun inserir(produtoCarrinhoModel: ProdutoCarrinhoModel)

    @Delete
    fun remover(produtoCarrinhoModel: ProdutoCarrinhoModel)
}