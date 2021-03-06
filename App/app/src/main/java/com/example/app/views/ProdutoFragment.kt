package com.example.app.views

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.annotation.UiThread
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.app.MainActivity
import com.example.app.R
import com.example.app.configs.buildCarrinhoDB
import com.example.app.configs.produtoExiste
import com.example.app.databinding.AtributoProdutoBinding
import com.example.app.databinding.FragmentProdutoBinding
import com.example.app.models.ProdutoCarrinhoModel
import com.example.app.models.ProdutoModel
import com.example.app.utils.converDoubleToPrice
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*


class ProdutoFragment : Fragment() {
    lateinit var binding: FragmentProdutoBinding
    lateinit var activity: MainActivity

    fun adicionarAtributo (chave: String, valor: String) {
        val atributoValorBinding = AtributoProdutoBinding.inflate(layoutInflater)
        atributoValorBinding.produtoAtributoChave.text = "${chave}:"
        atributoValorBinding.produtoAtributoValor.text = valor

        binding.produtoContainerAtributos.addView(atributoValorBinding.root)
    }

    fun setarLoading (loading: Boolean) {
        binding.botaoAdicionarCarrinho.isClickable = !loading
        binding.loading.visibility = if (loading) View.VISIBLE else View.INVISIBLE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProdutoBinding.inflate(inflater)
        activity = getActivity() as MainActivity

        val produto = arguments?.getParcelable<ProdutoModel>("produto") as ProdutoModel
        setarLoading(false)

        if (produto.estoque > 0) {
            binding.produtoEstoque.text = "${produto.estoque} ${getString(R.string.quantidade_em_estoque)}"
        } else {
            binding.produtoEstoque.text = getString(R.string.fora_de_estoque)
        }

        binding.produtoDescricaoProduto.text = produto.descricao
        binding.precoProduto.text = converDoubleToPrice(produto.preco)
        binding.precoPromocionalProduto.text = converDoubleToPrice(produto.preco_promocional)

        if (produto.preco_promocional > 0) {
            binding.precoProduto.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.precoProduto.textSize = 14F
            binding.precoProduto.setTextColor(R.color.black_faded.toInt())
        } else {
            binding.precoPromocionalProduto.visibility = View.INVISIBLE
        }

        val imageList = ArrayList<SlideModel>()
        produto.imagens.forEach {
            imageList.add(SlideModel(it, produto.nome))
        }
        binding.imageSlider.setImageList(imageList, ScaleTypes.CENTER_INSIDE)

        binding.produtoContainerAtributos.removeAllViews()
        adicionarAtributo("marca", produto.marca)
        produto.atributos?.forEach {
            adicionarAtributo(it.chave, it.valor)
        }

        binding.botaoAdicionarCarrinho.setOnClickListener {
            setarLoading(true)
            Thread {
                val carrinhoDB = buildCarrinhoDB(context as Context)
                val produtoCarrinho = produtoExiste(context as Context, ProdutoCarrinhoModel(produto.id))

                if (produtoCarrinho == null) {
                    carrinhoDB.inserir(ProdutoCarrinhoModel(produto.id))
                } else {
                    val quantidade = produtoCarrinho.quantidade as Int
                    carrinhoDB.atualizar_quantidade(produtoCarrinho.id_produto, quantidade + 1)
                }

                activity.runOnUiThread {
                    setarLoading(false)
                    activity.mostrarEsconderBotaoCarrinho(true)
                }
            }.start()
        }

        return binding.root
    }
}