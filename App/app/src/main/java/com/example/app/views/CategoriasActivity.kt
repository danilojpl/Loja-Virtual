package com.example.app.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.app.adapters.CategoriaRecyclerViewAdapter
import com.example.app.databinding.ActivityCategoriasBinding
import com.example.app.models.CategoriaModel
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CategoriasActivity : AppCompatActivity() {
    lateinit var binding: ActivityCategoriasBinding
    lateinit var database: DatabaseReference
    lateinit var adapter: CategoriaRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference

        val opcoes = FirebaseRecyclerOptions.Builder<CategoriaModel>()
            .setQuery(database.child("categorias"),CategoriaModel::class.java)
            .build()

        adapter = CategoriaRecyclerViewAdapter(opcoes)

        binding.recyclerView.adapter = adapter

        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)

        adapter.startListening()


    }

    override fun onStop() {
        super.onStop()
        adapter?.let {
            adapter.stopListening()
        }

    }
    override fun onResume() {
        super.onResume()

        adapter?.let {
            adapter.startListening()
        }

    }
}