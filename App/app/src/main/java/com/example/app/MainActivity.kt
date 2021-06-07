package com.example.app

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.app.configs.buildCarrinhoDB
import com.example.app.databinding.ActivityMainBinding
import com.example.app.views.*
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
    fun getUsuario(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    fun loginUsuario(){
        val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())

        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        startActivity(intent)
    }

    fun popUp(v: View, header:View){
        val popup = PopupMenu(this, v)
        popup.inflate(R.menu.popup)
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
            when(item?.itemId){
                R.id.logOut -> {
                    header.textLogin.text = ""
                    FirebaseAuth.getInstance().signOut()
                }
            }
            true
        })
        popup.show()

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
            var frag: Fragment

            when (it.itemId) {
                R.id.item_produtos -> {
                    frag = ListaProdutosFragment()
                }

                R.id.item_sobre -> {
                    frag = SobreFragment()
                }

                R.id.item_carrinho -> {
                    frag = CarrinhoFragment()
                }

                R.id.item_categorias -> {
                    frag = CategoriasFragment()
                }

                R.id.item_compras -> {
                    frag = ComprasFragment()
                }

                else -> {
                    frag = CategoriasFragment()
                }
            }

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragContainer, frag)
                .commit()

            true
        }

        val header = binding.navigationView.getHeaderView(0)
        val button = header.imageLogin

        button.setOnClickListener {
            if (getUsuario() == null){
               loginUsuario()
            }
            else{
                popUp(button,header)
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
    override fun onResume() {
        super.onResume()
        val header = binding.navigationView.getHeaderView(0)
        header.textLogin.text = FirebaseAuth.getInstance().currentUser?.email
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        toggle?.let {
            return it.onOptionsItemSelected(item)
        }

        return false
    }

    override fun onBackPressed() {
        val currentFrag = supportFragmentManager.findFragmentById(R.id.fragContainer)

        if (currentFrag is CategoriasFragment) {
            super.onBackPressed()
        }

        else if (currentFrag is ListaProdutosFragment) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragContainer, CategoriasFragment())
                .commit()
        }

        else {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragContainer, ListaProdutosFragment())
                .commit()
        }
    }
}