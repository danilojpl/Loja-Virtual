package com.example.app.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app.MainActivity
import com.example.app.R
import com.example.app.configs.buildServiceCompra
import com.example.app.configs.buildServiceProduto
import com.example.app.databinding.CardCompraBinding
import com.example.app.databinding.CardCompraProdutoBinding
import com.example.app.databinding.FragmentComprasBinding
import com.example.app.types.ListaCompraType
import com.example.app.types.ListaProdutoType
import com.example.app.utils.converDoubleToPrice
import com.example.app.utils.melhorPrecoProduto
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal


class ComprasFragment : Fragment() {
    lateinit var binding: FragmentComprasBinding
    lateinit var activity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentComprasBinding.inflate(inflater)
        activity = getActivity() as MainActivity

        carregarCompras()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        activity?.title = getString(R.string.tela_compras)
    }

    fun carregarCompras () {
        binding.loading.visibility = View.VISIBLE
        binding.textoInfo.visibility = View.INVISIBLE
        val user = activity.getUsuario()

        if (user == null) {
            binding.loading.visibility = View.INVISIBLE
            binding.textoInfo.visibility = View.VISIBLE
            binding.textoInfo.text = getString(R.string.logar_para_ver_compras)
        }

        else {
            val service = buildServiceCompra()
            val call = service.list("\"${user.uid}\"", "\"${user.uid}\\uf8ff\"")

            val callback = object: Callback<ListaCompraType> {
                override fun onResponse(call: Call<ListaCompraType>, response: Response<ListaCompraType>) {
                    if (response.isSuccessful) {
                        if (response.body().isNullOrEmpty()) {
                            binding.loading.visibility = View.INVISIBLE
                            binding.textoInfo.visibility = View.VISIBLE
                            binding.textoInfo.text = getString(R.string.sem_compras)
                        } else {
                            atualizarListaUI(response.body() as ListaCompraType)
                        }
                    } else {
                        Snackbar.make(binding.root, R.string.erro_lista_compras, Snackbar.LENGTH_LONG).show()
                        binding.loading.visibility = View.INVISIBLE
                    }
                }

                override fun onFailure(call: Call<ListaCompraType>, t: Throwable) {
                    Snackbar.make(binding.root, R.string.erro_internet, Snackbar.LENGTH_LONG).show()
                    binding.loading.visibility = View.INVISIBLE
                }
            }

            call.enqueue(callback)
        }
    }

    fun atualizarListaUI (lista: ListaCompraType) {
        lista.forEach {
            val compra = it
            val cardCompra = CardCompraBinding.inflate(layoutInflater)
            var total = BigDecimal(0)

            compra.value.produtos.forEach {
                val produto = it

                val service = buildServiceProduto()
                val call = service.findById("\"${produto.id_produto}\"", "\"${produto.id_produto}\\uf8ff\"")

                val callback = object: Callback<ListaProdutoType> {
                    override fun onResponse(call: Call<ListaProdutoType>, response: Response<ListaProdutoType>) {
                        if (response.isSuccessful) {
                            cardCompra.precoTotalCompra.visibility = View.INVISIBLE
                            response.body()?.forEach {
                                val cardProduto = CardCompraProdutoBinding.inflate(layoutInflater)

                                cardProduto.compraProdutoNome.text = it.value.nome
                                cardProduto.compraProdutoPreco.text = converDoubleToPrice(melhorPrecoProduto(it.value))
                                cardProduto.compraProdutoQuantidade.text = "${produto.quntidade}x"

                                Picasso.get()
                                    .load(it.value.imagens[0])
                                    .into(cardProduto.compraProdutoImagem)

                                total += BigDecimal(produto.quntidade) * BigDecimal(melhorPrecoProduto(it.value))
                                cardCompra.containerProdutos.addView(cardProduto.root)
                            }

                            cardCompra.precoTotalCompra.text = converDoubleToPrice(total.toDouble())
                            cardCompra.precoTotalCompra.visibility = View.VISIBLE
                        } else {
                            Snackbar.make(binding.container, R.string.erro_lista_produtos, Snackbar.LENGTH_LONG).show()
                            binding.loading.visibility = View.INVISIBLE
                        }
                    }

                    override fun onFailure(call: Call<ListaProdutoType>, t: Throwable) {
                        Snackbar.make(binding.container, R.string.erro_internet, Snackbar.LENGTH_LONG).show()
                        binding.loading.visibility = View.INVISIBLE
                    }
                }

                call.enqueue(callback)
            }

            binding.container.addView(cardCompra.root)
            binding.loading.visibility = View.INVISIBLE
        }
    }
}