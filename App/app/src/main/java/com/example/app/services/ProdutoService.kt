package com.example.app.services

import com.example.app.types.ListaProdutoType
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ProdutoService {
    @GET("/produtos.json?auth=7iCXnlTCgJdNKmu2Vtjfx6wNGWe1PyLynyx4vLT4")
    fun list(): Call<ListaProdutoType>

    @GET("/produtos.json?auth=7iCXnlTCgJdNKmu2Vtjfx6wNGWe1PyLynyx4vLT4&orderBy=\"nome\"")
    fun filterByName(
        @Query("startAt") startAt: String,
        @Query("endAt") endAt: String
    ): Call<ListaProdutoType>

    @GET("/produtos.json?auth=7iCXnlTCgJdNKmu2Vtjfx6wNGWe1PyLynyx4vLT4&orderBy=\"id\"")
    fun findById(
        @Query("startAt") startAt: String,
        @Query("endAt") endAt: String
    ): Call<ListaProdutoType>
}