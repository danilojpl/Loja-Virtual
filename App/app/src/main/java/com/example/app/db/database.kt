package com.example.app.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.app.models.ProdutoCarrinhoModel

@Database(entities = arrayOf(ProdutoCarrinhoModel::class), version = 4)
abstract class database : RoomDatabase() {
    abstract fun ProdutoCarrinhoDAO(): ProdutoCarrinhoDAO
}
