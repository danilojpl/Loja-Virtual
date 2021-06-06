package com.example.app.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.app.R
import com.example.app.adapters.CategoriaRecyclerViewAdapter
import com.example.app.databinding.FragmentCategoriasBinding
import com.example.app.models.CategoriaModel
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CategoriasFragment : Fragment() {

    lateinit var binding: FragmentCategoriasBinding
    lateinit var database: DatabaseReference
    lateinit var adapter: CategoriaRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentCategoriasBinding.inflate(inflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        configurarFirebase()
        adapter?.let {
            adapter.startListening()
        }
    }

    override fun onStop() {
        super.onStop()
        adapter?.let {
            adapter.stopListening()
        }

    }

    fun configurarFirebase(){
        database = FirebaseDatabase.getInstance().reference

        val opcoes = FirebaseRecyclerOptions.Builder<CategoriaModel>()
            .setQuery(database.child("categorias"), CategoriaModel::class.java)
            .build()

        adapter = CategoriaRecyclerViewAdapter(opcoes,activity)

        binding.recyclerView.adapter = adapter

        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2,
            StaggeredGridLayoutManager.VERTICAL)

        adapter.startListening()
    }


}