package com.example.app.views

import android.app.Activity
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.app.R
import com.example.app.configs.buildServiceProduto
import com.example.app.databinding.CardProdutoBinding
import com.example.app.databinding.FragmentListaProdutosViewBinding
import com.example.app.models.ProdutoModel
import com.example.app.types.ListaProdutoType
import com.example.app.utils.converDoubleToPrice
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListaProdutosFragment : Fragment() {
    lateinit var binding: FragmentListaProdutosViewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): FrameLayout {
        binding = FragmentListaProdutosViewBinding.inflate(inflater)

        binding.inputNomeProduto.setOnKeyListener { _, key, event ->
            if (key == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                pesquisarProdutos(binding.inputNomeProduto.text.toString())

                val keyboard = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                keyboard.hideSoftInputFromWindow(view?.windowToken, 0)

                true
            }

            false
        }

        binding.inputNomeContainer.setEndIconOnClickListener {
            binding.inputNomeProduto.setText("")
            atualizarProdutos()
        }

        return binding.root
    }

    fun pesquisarProdutos (nome: String) {
        val service = buildServiceProduto()
        val call = service.filterByName("\"${nome}\"", "\"${nome}\\uf8ff\"")

        val callback = object: Callback<ListaProdutoType> {
            override fun onResponse(call: Call<ListaProdutoType>, response: Response<ListaProdutoType>) {
                if (response.isSuccessful) {
                    atualizarListaUI(response.body(), null)
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
        binding.naoHaItens.visibility = View.INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        val categoria = this.arguments?.getString("categoria")

        atualizarProdutos(categoria)
        activity?.title = getString(R.string.tela_lista_produtos)
    }

    fun atualizarProdutos (categoria: String? = null) {
        val service = buildServiceProduto()
        val call = service.list()

        val callback = object: Callback<ListaProdutoType> {
            override fun onResponse(call: Call<ListaProdutoType>, response: Response<ListaProdutoType>) {
                if (response.isSuccessful) {
                    atualizarListaUI(response.body(), categoria)
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
        binding.naoHaItens.visibility = View.INVISIBLE
    }

    fun abrirDetalhesProduto (produto: ProdutoModel) {
        val bundle = Bundle()
        bundle.putParcelable("produto", produto)

        val fragment = ProdutoFragment()
        fragment.arguments = bundle
        parentFragmentManager.beginTransaction().replace(R.id.fragContainer, fragment).commit()
    }

    fun atualizarListaUI (lista: ListaProdutoType?, categoria: String? = null) {
        binding.container.removeAllViews()

        if (lista?.size == 0) {
            binding.naoHaItens.visibility = View.VISIBLE
        } else {
            lista?.forEach {
                if (it.value.categoria == categoria || categoria == null) {
                    val cardBinding = CardProdutoBinding.inflate(layoutInflater)
                    val produto = it.value

                    cardBinding.produtoNome.text = produto.nome
                    cardBinding.produtoDescricao.text = produto.descricao
                    cardBinding.produtoPreco.text = converDoubleToPrice(produto.preco)

                    cardBinding.produtoContainer.setOnClickListener {
                        abrirDetalhesProduto(produto)
                    }

                    Picasso.get()
                        .load(it.value.imagens[0])
                        .into(cardBinding.produtoImagem)

                    binding.container.addView(cardBinding.root)
                }
            }
        }

        binding.loading.visibility = View.INVISIBLE
    }
}