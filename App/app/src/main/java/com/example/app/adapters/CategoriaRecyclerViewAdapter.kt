package com.example.app.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.app.MainActivity
import com.example.app.databinding.RowCategoriaBinding
import com.example.app.models.CategoriaModel
import com.example.app.views.CategoriasActivity
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.squareup.picasso.Picasso

class CategoriaRecyclerViewAdapter (options: FirebaseRecyclerOptions<CategoriaModel>):
FirebaseRecyclerAdapter<CategoriaModel, CategoriaRecyclerViewAdapter.CategoriaViewHolder>(options){

    class CategoriaViewHolder(val binding: RowCategoriaBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(categoria: CategoriaModel){
            Picasso.get()
                .load(categoria.imagem)
                .into(binding.imageCat)
            binding.textNameCat.text = categoria.nome

            binding.imageCat.setOnClickListener {
                val i = Intent(binding.imageCat.context, MainActivity::class.java)
                i.putExtra("categoria", categoria.id)
                binding.imageCat.context.startActivity(i)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowCategoriaBinding.inflate(layoutInflater, parent, false)

        return CategoriaViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CategoriaViewHolder,
        position: Int,
        model: CategoriaModel
    ) {
        holder.bind(model)
    }
}