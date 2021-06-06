package com.example.app.configs

import com.example.app.services.ProdutoService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun buildRetrofit ():Retrofit {
    return Retrofit
        .Builder()
        .baseUrl("https://lojavirtual-79137-default-rtdb.firebaseio.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun buildServiceProduto (): ProdutoService {
    return buildRetrofit().create(ProdutoService::class.java)
}
