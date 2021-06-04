package com.example.app.services

import com.example.app.types.ListaProdutoType
import retrofit2.Call
import retrofit2.http.GET

interface ProdutoService {
    @GET("/produtos.json?auth=7iCXnlTCgJdNKmu2Vtjfx6wNGWe1PyLynyx4vLT4")
    fun list(): Call<ListaProdutoType>
}