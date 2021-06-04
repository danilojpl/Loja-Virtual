package com.example.app.services

import com.example.app.models.ProdutoModel
import retrofit2.Call
import retrofit2.http.GET

interface ProdutoService {
    @GET("/produtos.json")
    fun list(): Call<List<ProdutoModel>>
}