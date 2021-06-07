package com.example.app.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.allViews
import androidx.core.view.size
import androidx.fragment.app.Fragment
import com.example.app.MainActivity
import com.example.app.R
import com.example.app.configs.buildCarrinhoDB
import com.example.app.configs.buildServiceCompra
import com.example.app.configs.buildServiceProduto
import com.example.app.databinding.CardProdutoCarrinhoBinding
import com.example.app.databinding.FragmentCarrinhoBinding
import com.example.app.models.CompraModel
import com.example.app.models.ProdutoCarrinhoModel
import com.example.app.models.ProdutoCompraModel
import com.example.app.models.ProdutoModel
import com.example.app.types.FirebasePostResponseType
import com.example.app.types.ListaProdutoType
import com.example.app.utils.converDoubleToPrice
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_produto_carrinho.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

class CarrinhoFragment : Fragment() {
    lateinit var binding: FragmentCarrinhoBinding
    lateinit var activity: MainActivity
    var valorTotal = 0.0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCarrinhoBinding.inflate(inflater)
        activity = getActivity() as MainActivity

        atualizarCarrinho()

        binding.botaoFinalizarCompra.setOnClickListener {
            finalizarCompra()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        activity?.title = getString(R.string.tela_carrinho)
        activity.mostrarEsconderBotaoCarrinho(false, true)
    }

    fun acaoBotoes (ativado: Boolean) {
        binding.botaoFinalizarCompra.isClickable = ativado
        for (view in binding.container.allViews) {
            if (view.id == R.layout.card_produto_carrinho) {
                view.botao_decrescimo.isClickable = ativado
                view.botao_incremento.isClickable = ativado
            }
        }
    }

    fun finalizarCompra () {
        val user = activity.getUsuario()

        if (user == null){
            activity.loginUsuario()
        }

        else{
            binding.loading.visibility = View.VISIBLE
            acaoBotoes(false)

            Thread {
                val carrinhoDB = buildCarrinhoDB(activity)
                val produtosCarrinho = carrinhoDB.listarTodos()
                val produtosCompra = produtosCarrinho.map {
                    val quantidade = it.quantidade as Int
                    ProdutoCompraModel(it.id_produto, quantidade.toString())
                }
                val compra = CompraModel(user.uid, produtosCompra)

                activity.runOnUiThread {
                    val service = buildServiceCompra()
                    val call = service.efetivarCompra(compra)

                    val callback = object: Callback<FirebasePostResponseType> {
                        override fun onResponse(call: Call<FirebasePostResponseType>, response: Response<FirebasePostResponseType>) {
                            if (response.isSuccessful) {
                                Snackbar.make(binding.container, R.string.compra_finalizada, Snackbar.LENGTH_LONG).show()

                                Thread {
                                    produtosCarrinho.forEach {
                                        carrinhoDB.remover(it)
                                    }

                                    activity.runOnUiThread {
                                        activity.supportFragmentManager
                                            .beginTransaction()
                                            .replace(R.id.fragContainer, ListaProdutosFragment())
                                            .commit()
                                    }
                                }.start()
                            } else {
                                Snackbar.make(binding.container, R.string.erro_finalizar_compra, Snackbar.LENGTH_LONG).show()
                                binding.loading.visibility = View.INVISIBLE
                                acaoBotoes(true)
                            }
                        }

                        override fun onFailure(call: Call<FirebasePostResponseType>, t: Throwable) {
                            Snackbar.make(binding.container, R.string.erro_internet, Snackbar.LENGTH_LONG).show()
                            binding.loading.visibility = View.INVISIBLE
                            Log.e("hmm", t.toString())
                            acaoBotoes(true)
                        }
                    }

                    call.enqueue(callback)
                }
            }.start()
        }
    }

    fun bindCard (produtoCarrinho: ProdutoCarrinhoModel, produto: ProdutoModel, card: CardProdutoCarrinhoBinding) {
        val quantidade = produtoCarrinho.quantidade as Int
        val valor = if (produto.preco_promocional > 0F) produto.preco_promocional else produto.preco

        card.produtoPreco.text = converDoubleToPrice(valor)
        card.produtoPrecoTotal.text = converDoubleToPrice(quantidade * valor)
        card.produtoQuantidade.text = "x${quantidade}"


        card.botaoIncremento.setOnClickListener {
            if (quantidade < produto.estoque) {
                val novoProdutoCarrinho = ProdutoCarrinhoModel(produto.id, quantidade + 1)
                atualizarValor(novoProdutoCarrinho)
                bindCard(novoProdutoCarrinho, produto, card)

                valorTotal += valor
                binding.totalCompra.text = converDoubleToPrice(valorTotal)
            } else {
                Snackbar.make(binding.container, R.string.sem_estoque, Snackbar.LENGTH_LONG).show()
            }
        }

        card.botaoDecrescimo.setOnClickListener {
            valorTotal -= valor
            binding.totalCompra.text = converDoubleToPrice(valorTotal)

            if (quantidade as Int > 1) {
                val novoProdutoCarrinho = ProdutoCarrinhoModel(produto.id, quantidade - 1)
                atualizarValor(novoProdutoCarrinho)
                bindCard(novoProdutoCarrinho, produto, card)
            } else {
                removerProduto(produtoCarrinho, card)
            }
        }

    }

    fun removerProduto (produto: ProdutoCarrinhoModel, card: CardProdutoCarrinhoBinding) {
        binding.loading.visibility = View.VISIBLE
        val carrinhoDB = buildCarrinhoDB(activity)

        Thread {
            carrinhoDB.remover(produto)
            activity.runOnUiThread {
                binding.loading.visibility = View.INVISIBLE
                binding.container.removeView(card.root)

                if (binding.container.childCount == 0) {
                    binding.naoHaItens.visibility = View.VISIBLE
                    binding.totalCompra.visibility = View.INVISIBLE
                    binding.botaoFinalizarCompra.visibility = View.INVISIBLE
                }
            }
        }.start()
    }

    fun atualizarValor (produtoCarrinho: ProdutoCarrinhoModel) {
        binding.loading.visibility = View.VISIBLE
        val carrinhoDB = buildCarrinhoDB(activity)

        Thread {
            carrinhoDB.atualizar_quantidade(produtoCarrinho.id_produto, produtoCarrinho.quantidade as Int)
            activity.runOnUiThread {
                binding.loading.visibility = View.INVISIBLE
            }
        }.start()
    }

    fun adicionarItem(produto: ProdutoModel, quantidade: Int) {
        val card = CardProdutoCarrinhoBinding.inflate(layoutInflater)
        val valor = if (produto.preco_promocional > 0F) produto.preco_promocional else produto.preco
        valorTotal += valor

        card.produtoNome.text = produto.nome
        card.produtoPreco.text = converDoubleToPrice(valor)

        Picasso.get()
            .load(produto.imagens[0])
            .into(card.produtoImagem)

        binding.container.addView(card.root)

        bindCard(ProdutoCarrinhoModel(produto.id, quantidade), produto, card)

        binding.loading.visibility = View.INVISIBLE
        binding.totalCompra.text = converDoubleToPrice(valorTotal)
        binding.totalCompra.visibility = View.VISIBLE
        binding.botaoFinalizarCompra.visibility = View.VISIBLE
    }

    fun atualizarCarrinho() {
        binding.loading.visibility = View.VISIBLE
        binding.naoHaItens.visibility = View.INVISIBLE
        binding.totalCompra.visibility = View.INVISIBLE
        binding.botaoFinalizarCompra.visibility = View.INVISIBLE
        binding.container.removeAllViews()

        val carrinhoDB = buildCarrinhoDB(activity)
        Thread {
            val produtosCarrinho = carrinhoDB.listarTodos()

            activity.runOnUiThread {
                if (produtosCarrinho.isEmpty()) {
                    binding.loading.visibility = View.INVISIBLE
                    binding.naoHaItens.visibility = View.VISIBLE
                }

                else {
                    produtosCarrinho.forEach {
                        val produtoCarrinho = it

                        val service = buildServiceProduto()
                        val call = service.findById("\"${produtoCarrinho.id_produto}\"", "\"${produtoCarrinho.id_produto}\\uf8ff\"")

                        val callback = object: Callback<ListaProdutoType> {
                            override fun onResponse(call: Call<ListaProdutoType>, response: Response<ListaProdutoType>) {
                                if (response.isSuccessful) {
                                    response.body()?.forEach {
                                        adicionarItem(it.value, produtoCarrinho.quantidade as Int)
                                    }
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
                        binding.loading.visibility = View.VISIBLE
                    }
                }
            }
        }.start()
    }
}