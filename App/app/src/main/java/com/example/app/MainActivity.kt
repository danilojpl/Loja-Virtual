package com.example.app

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.example.app.databinding.ActivityMainBinding
import com.example.app.views.ListaProdutosFragment
import com.example.app.views.ProdutoFragment
import com.example.app.views.SobreFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var toggle: ActionBarDrawerToggle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //id categoria
        var extras = getIntent().getExtras()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.openDrawer, R.string.closeDrawer)
        toggle?.let {
            binding.drawerLayout.addDrawerListener(it)
            it.syncState()
        }

        if (extras != null){
            val fragment = ListaProdutosFragment()
            fragment.arguments = extras
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragContainer, ListaProdutosFragment())
                .commit()
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

            true
        }
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
        } else {
            super.onBackPressed()
        }
    }
}