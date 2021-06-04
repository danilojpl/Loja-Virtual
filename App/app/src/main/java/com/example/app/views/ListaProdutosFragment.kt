package com.example.app.views

import android.opengl.Visibility
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
import com.example.app.utils.converDoubleToPrice
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
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
                    Snackbar.make(binding.container, "Não foi possível carregar os produtos", Snackbar.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ListaProdutoType>, t: Throwable) {
                Snackbar.make(binding.container, "Não foi possível se conectar a internet", Snackbar.LENGTH_LONG).show()
            }
        }

        call.enqueue(callback)
        binding.loading.visibility = View.VISIBLE
    }

    fun atualizarListaUI (lista: ListaProdutoType?) {
        binding.container.removeAllViews()

        lista?.forEach {
            val cardBinding = CardProdutoBinding.inflate(layoutInflater)
            cardBinding.produtoNome.text = it.value.nome
            cardBinding.produtoDescricao.text = it.value.descricao
            cardBinding.produtoPreco.text = converDoubleToPrice(it.value.preco)

            Picasso.get()
                .load(it.value.imagens[0])
                .into(cardBinding.produtoImagem)

            binding.container.addView(cardBinding.root)
        }

        binding.loading.visibility = View.INVISIBLE
    }
}