package com.example.app.services

import com.example.app.models.CompraModel
import com.example.app.types.FirebasePostResponseType
import com.example.app.types.ListaCompraType
import retrofit2.Call
import retrofit2.http.*

interface CompraService {
    @GET("/compras.json?auth=7iCXnlTCgJdNKmu2Vtjfx6wNGWe1PyLynyx4vLT4&orderBy=\"id_usuario\"")
    fun list(
        @Query("startAt") startAt: String,
        @Query("endAt") endAt: String
    ): Call<ListaCompraType>

    @Headers("Content-type: application/json")
    @POST("/compras.json?auth=7iCXnlTCgJdNKmu2Vtjfx6wNGWe1PyLynyx4vLT4")
    fun efetivarCompra(@Body data: CompraModel): Call<FirebasePostResponseType>
}