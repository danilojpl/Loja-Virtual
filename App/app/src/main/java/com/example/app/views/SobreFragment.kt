package com.example.app.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.app.R
import com.example.app.databinding.CardAutorBinding
import com.example.app.databinding.FragmentSobreBinding
import com.squareup.picasso.Picasso


class SobreFragment : Fragment() {
    lateinit var binding: FragmentSobreBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSobreBinding.inflate(inflater)

        arrayListOf(
            arrayListOf("Danilo Duarte", "https://avatars.githubusercontent.com/u/49461554?v=4", "danilo.duartejpl@gmail.com"),
            arrayListOf("Victor S. Costa", "https://avatars.githubusercontent.com/u/29923690?v=4", "victorsilva7p@gmail.com")
        ).forEach { autor ->
            val card = CardAutorBinding.inflate(layoutInflater)

            card.autorNome.text = autor[0]
            Picasso.get().load(autor[1]).into(card.autorImagem)

            card.botaoEnviarEmail.setOnClickListener {
                enviarEmail(autor[2])
            }

            binding.sobreContainerAutores.addView(card.root)
        }

        val senac = CardAutorBinding.inflate(layoutInflater)
        senac.autorNome.text = "SENAC"
        Picasso.get().load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSI9P9zJvY3OsbLpetDn0psWtM_7KaVqTw4eg&usqp=CAU").into(senac.autorImagem)

        senac.botaoEnviarEmail.text = getString(R.string.abrir_site)
        senac.botaoEnviarEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.sp.senac.br/"))
            startActivity(intent)
        }

        binding.conatinerSenac.addView(senac.root)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        activity?.title = getString(R.string.tela_sobre)
    }

    fun enviarEmail (email: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:${email}")
        startActivity(intent)
    }
}