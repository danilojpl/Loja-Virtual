package com.example.app

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.example.app.configs.buildCarrinhoDB
import com.example.app.databinding.ActivityMainBinding
import com.example.app.views.CategoriasFragment
import com.example.app.views.CarrinhoFragment
import com.example.app.views.ListaProdutosFragment
import com.example.app.views.ProdutoFragment
import com.example.app.views.SobreFragment
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.menu_cabecalho.view.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var toggle: ActionBarDrawerToggle? = null

    fun mostrarEsconderBotaoCarrinho (adicionadoRecente: Boolean = false, telaCarrinho: Boolean = false) {
        binding.botaoCarrinho.visibility = View.INVISIBLE

        if (telaCarrinho) {
            binding.botaoCarrinho.visibility = View.INVISIBLE
        } else if (adicionadoRecente) {
            binding.botaoCarrinho.visibility = View.VISIBLE
        } else {
            Thread {
                val produtosCarrinho = buildCarrinhoDB(this).listarTodos()

                runOnUiThread {
                    if (produtosCarrinho.isNotEmpty()) {
                        binding.botaoCarrinho.visibility = View.VISIBLE
                    }
                }
            }.start()
        }
    }
    fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth .getInstance().currentUser
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction().replace(R.id.fragContainer, CategoriasFragment()).commit()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.openDrawer, R.string.closeDrawer)
        toggle?.let {
            binding.drawerLayout.addDrawerListener(it)
            it.syncState()
        }

        binding.navigationView.setNavigationItemSelectedListener {
            binding.drawerLayout.closeDrawers()

            if (it.itemId == R.id.item_produtos) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragContainer, ListaProdutosFragment())
                    .commit()
            }

            else if (it.itemId == R.id.item_sobre) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragContainer, SobreFragment())
                    .commit()
            }

            else if (it.itemId == R.id.item_carrinho) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragContainer, CarrinhoFragment())
                    .commit()
            }

            true
        }
        val header = binding.navigationView.getHeaderView(0)
        val button = header.imageLogin
        button.setOnClickListener {
            if (getCurrentUser() == null){
                val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())

                val intent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build()
               startActivity(intent)
            }
        }
        mostrarEsconderBotaoCarrinho()

        binding.botaoCarrinho.setOnClickListener {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragContainer, CarrinhoFragment())
                .commit()
        }

        mostrarEsconderBotaoCarrinho(false, false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        toggle?.let {
            return it.onOptionsItemSelected(item)
        }

        return false
    }

    override fun onBackPressed() {
        val currentFrag = supportFragmentManager.findFragmentById(R.id.fragContainer)

        if (currentFrag is ProdutoFragment) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragContainer, ListaProdutosFragment())
                .commit()
        }

        else if (currentFrag is CarrinhoFragment) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragContainer, ListaProdutosFragment())
                .commit()
        }

        else {
            super.onBackPressed()
        }
    }
}