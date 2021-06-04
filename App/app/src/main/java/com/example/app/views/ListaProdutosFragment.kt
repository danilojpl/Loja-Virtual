package com.example.app.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.app.configs.buildRetrofit
import com.example.app.configs.buildServiceProduto
import com.example.app.databinding.CardProdutoBinding
import com.example.app.databinding.FragmentListaProdutosViewBinding
import com.example.app.models.ProdutoModel
import com.example.app.types.ListaProdutoType
import com.google.android.material.card.MaterialCardView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class ListaProdutosFragment : Fragment() {
    lateinit var binding: FragmentListaProdutosViewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentListaProdutosViewBinding.inflate(inflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        atualizarProdutos()
    }

    fun atualizarProdutos () {
        val service = buildServiceProduto()
        val call = service.list()

        val callback = object: Callback<ListaProdutoType> {
            override fun onResponse(call: Call<ListaProdutoType>, response: Response<ListaProdutoType>) {
                if (response.isSuccessful) {
                    atualizarListaUI(response.body())
                } else {
                    Log.e("TAG1", response.body().toString())
                }
            }

            override fun onFailure(call: Call<ListaProdutoType>, t: Throwable) {
                Log.e("TAG2", t.toString())
            }
        }

        call.enqueue(callback)
    }

    fun atualizarListaUI (lista: ListaProdutoType?) {
        binding.container.removeAllViews()

        lista?.forEach {
            val cardBinding = CardProdutoBinding.inflate(layoutInflater)
            cardBinding.nome.text = it.value.nome

            binding.container.addView(cardBinding.root)
        }
    }
}