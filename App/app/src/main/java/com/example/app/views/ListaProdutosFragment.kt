package com.example.app.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.app.R
import com.example.app.configs.buildServiceProduto
import com.example.app.databinding.CardProdutoBinding
import com.example.app.databinding.FragmentListaProdutosViewBinding
import com.example.app.types.ListaProdutoType
import com.example.app.utils.converDoubleToPrice
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListaProdutosFragment : Fragment() {
    lateinit var binding: FragmentListaProdutosViewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentListaProdutosViewBinding.inflate(inflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val categoria = this.arguments?.getString("categoria")

        atualizarProdutos(categoria)
    }

    fun atualizarProdutos (categoria: String?) {
        val service = buildServiceProduto()
        val call = service.list()

        val callback = object: Callback<ListaProdutoType> {
            override fun onResponse(call: Call<ListaProdutoType>, response: Response<ListaProdutoType>) {
                if (response.isSuccessful) {
                    atualizarListaUI(response.body(), categoria)
                } else {
                    Snackbar.make(binding.container, R.string.erro_lista_produtos, Snackbar.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ListaProdutoType>, t: Throwable) {
                Snackbar.make(binding.container, R.string.erro_internet, Snackbar.LENGTH_LONG).show()
            }
        }

        call.enqueue(callback)
        binding.loading.visibility = View.VISIBLE
    }

    fun atualizarListaUI (lista: ListaProdutoType?, categoria: String?) {
        binding.container.removeAllViews()

        lista?.forEach {
            if (it.value.categoria == categoria || categoria == null) {
                val cardBinding = CardProdutoBinding.inflate(layoutInflater)
                cardBinding.produtoNome.text = it.value.nome
                cardBinding.produtoDescricao.text = it.value.descricao
                cardBinding.produtoPreco.text = converDoubleToPrice(it.value.preco)

                Picasso.get()
                    .load(it.value.imagens[0])
                    .into(cardBinding.produtoImagem)

                binding.container.addView(cardBinding.root)
            }
        }

        binding.loading.visibility = View.INVISIBLE
    }
}