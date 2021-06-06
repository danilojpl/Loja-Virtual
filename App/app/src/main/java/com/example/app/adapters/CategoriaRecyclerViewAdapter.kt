package com.example.app.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.databinding.RowCategoriaBinding
import com.example.app.models.CategoriaModel
import com.example.app.views.ListaProdutosFragment
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.squareup.picasso.Picasso

class CategoriaRecyclerViewAdapter (options: FirebaseRecyclerOptions<CategoriaModel>, context: Context?):
FirebaseRecyclerAdapter<CategoriaModel, CategoriaRecyclerViewAdapter.CategoriaViewHolder>(options){
    val contexto = context
     inner class CategoriaViewHolder(val binding: RowCategoriaBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(categoria: CategoriaModel) {
            Picasso.get()
                .load(categoria.imagem)
                .into(binding.imageCat)
            binding.textNameCat.text = categoria.nome
            binding.imageCat.setOnClickListener {
                var bundle = Bundle()
                bundle.putString("categoria",categoria.id)
                val fragment = ListaProdutosFragment()
                fragment.arguments = bundle
                val manager  = (contexto as AppCompatActivity).supportFragmentManager.beginTransaction().replace(R.id.fragContainer, fragment).commit()
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