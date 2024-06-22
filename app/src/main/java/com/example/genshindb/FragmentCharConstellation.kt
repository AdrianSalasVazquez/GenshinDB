package com.example.genshindb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.genshindb.databinding.FragmentCharConstellationBinding
import com.squareup.picasso.Picasso
import java.util.*

class FragmentCharConstellation : Fragment() {

    private lateinit var binding: FragmentCharConstellationBinding
    private lateinit var character: CharactersResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCharConstellationBinding.inflate(layoutInflater)
        arguments?.let {
            character = it.getSerializable(ARG_PERSONAJE) as CharactersResponse
        }

        //Construccion del interfaz
        construccionInterfazConstelaciones()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        private const val ARG_PERSONAJE = "personaje_serializable"

        // Método de acompañamiento para crear una nueva instancia del fragmento con el personaje como argumento
        fun newInstance(personaje: CharactersResponse): FragmentCharConstellation {
            val fragment = FragmentCharConstellation()
            val args = Bundle()
            args.putSerializable(ARG_PERSONAJE, personaje)
            fragment.arguments = args
            return fragment
        }
    }

    private fun construccionInterfazConstelaciones() {

        //Dependiendo del elemento del personaje
        val colorElemento : Int
        val colorElementoDark : Int
        val backgroundElemento : Int
        val boxStyleElemento : Int
        when (character.visionKey) {
            "ANEMO" -> {
                colorElemento = R.color.anemo
                colorElementoDark = R.color.anemo_dark
                backgroundElemento = R.drawable.background_anemo
                boxStyleElemento = R.drawable.box_style_anemo
            }
            "CRYO" -> {
                colorElemento = R.color.cryo
                colorElementoDark = R.color.cryo_dark
                backgroundElemento = R.drawable.background_cryo
                boxStyleElemento = R.drawable.box_style_cryo
            }
            "DENDRO" -> {
                colorElemento = R.color.dendro
                colorElementoDark = R.color.dendro_dark
                backgroundElemento = R.drawable.background_dendro
                boxStyleElemento = R.drawable.box_style_dendro
            }
            "ELECTRO" -> {
                colorElemento = R.color.electro
                colorElementoDark = R.color.electro_dark
                backgroundElemento = R.drawable.background_electro
                boxStyleElemento = R.drawable.box_style_electro
            }
            "GEO" -> {
                colorElemento = R.color.geo
                colorElementoDark = R.color.geo_dark
                backgroundElemento = R.drawable.background_geo
                boxStyleElemento = R.drawable.box_style_geo
            }
            "HYDRO" -> {
                colorElemento = R.color.hydro
                colorElementoDark = R.color.hydro_dark
                backgroundElemento = R.drawable.background_hydro
                boxStyleElemento = R.drawable.box_style_hydro
            }
            "PYRO" -> {
                colorElemento = R.color.pyro
                colorElementoDark = R.color.pyro_dark
                backgroundElemento = R.drawable.background_pyro
                boxStyleElemento = R.drawable.box_style_pyro
            }
            else -> {
                colorElemento = 0
                colorElementoDark = 0
                backgroundElemento = 0
                boxStyleElemento = 0
            }
        }
        //Recuperando los recursos
        val colorElementoRecurso = context?.let { ContextCompat.getColor(it, colorElemento) }
        val colorElementoDarkRecurso = context?.let { ContextCompat.getColor(it, colorElementoDark) }
        val backgroundElementoRecurso = context?.let { ContextCompat.getDrawable(it,backgroundElemento) }
        val backgroundElementoRecursoIcon = context?.let { ContextCompat.getDrawable(it,backgroundElemento) }

        // Cambio del background segun elemento
        binding.scrollConstelaciones.background = backgroundElementoRecurso

        // Cambio del background en icono de constelacion
        binding.backgroundIconC1.background = backgroundElementoRecursoIcon
        binding.backgroundIconC2.background = backgroundElementoRecursoIcon
        binding.backgroundIconC3.background = backgroundElementoRecursoIcon
        binding.backgroundIconC4.background = backgroundElementoRecursoIcon
        binding.backgroundIconC5.background = backgroundElementoRecursoIcon
        binding.backgroundIconC6.background = backgroundElementoRecursoIcon

        //Añadir iconos de las constelaciones
        Picasso.get().load(character.resources.iconConstellations.iconC1URL).into(binding.iconC1)
        Picasso.get().load(character.resources.iconConstellations.iconC2URL).into(binding.iconC2)
        Picasso.get().load(character.resources.iconConstellations.iconC3URL).into(binding.iconC3)
        Picasso.get().load(character.resources.iconConstellations.iconC4URL).into(binding.iconC4)
        Picasso.get().load(character.resources.iconConstellations.iconC5URL).into(binding.iconC5)
        Picasso.get().load(character.resources.iconConstellations.iconC6URL).into(binding.iconC6)

        //Introduccion de textos por idioma
        if (Locale.getDefault().displayLanguage.lowercase() == "español") {
            //C1
            binding.nameC1.text = character.spanish.constellations.c1.name
            binding.unlockC1.text = character.spanish.constellations.c1.unlock
            binding.descC1.text = character.spanish.constellations.c1.description
            //C2
            binding.nameC2.text = character.spanish.constellations.c2.name
            binding.unlockC2.text = character.spanish.constellations.c2.unlock
            binding.descC2.text = character.spanish.constellations.c2.description
            //C3
            binding.nameC3.text = character.spanish.constellations.c3.name
            binding.unlockC3.text = character.spanish.constellations.c3.unlock
            binding.descC3.text = character.spanish.constellations.c3.description
            //C4
            binding.nameC4.text = character.spanish.constellations.c4.name
            binding.unlockC4.text = character.spanish.constellations.c4.unlock
            binding.descC4.text = character.spanish.constellations.c4.description
            //C5
            binding.nameC5.text = character.spanish.constellations.c5.name
            binding.unlockC5.text = character.spanish.constellations.c5.unlock
            binding.descC5.text = character.spanish.constellations.c5.description
            //C6
            binding.nameC6.text = character.spanish.constellations.c6.name
            binding.unlockC6.text = character.spanish.constellations.c6.unlock
            binding.descC6.text = character.spanish.constellations.c6.description
        }
        else{
            //C1
            binding.nameC1.text = character.english.constellations.c1.name
            binding.unlockC1.text = character.english.constellations.c1.unlock
            binding.descC1.text = character.english.constellations.c1.description
            //C2
            binding.nameC2.text = character.english.constellations.c2.name
            binding.unlockC2.text = character.english.constellations.c2.unlock
            binding.descC2.text = character.english.constellations.c2.description
            //C3
            binding.nameC3.text = character.english.constellations.c3.name
            binding.unlockC3.text = character.english.constellations.c3.unlock
            binding.descC3.text = character.english.constellations.c3.description
            //C4
            binding.nameC4.text = character.english.constellations.c4.name
            binding.unlockC4.text = character.english.constellations.c4.unlock
            binding.descC4.text = character.english.constellations.c4.description
            //C5
            binding.nameC5.text = character.english.constellations.c5.name
            binding.unlockC5.text = character.english.constellations.c5.unlock
            binding.descC5.text = character.english.constellations.c5.description
            //C6
            binding.nameC6.text = character.english.constellations.c6.name
            binding.unlockC6.text = character.english.constellations.c6.unlock
            binding.descC6.text = character.english.constellations.c6.description
        }

        //Cambiando a color de elemento a textos
        if (colorElementoRecurso != null) {
            binding.unlockC1.setTextColor(colorElementoRecurso)
            binding.unlockC2.setTextColor(colorElementoRecurso)
            binding.unlockC3.setTextColor(colorElementoRecurso)
            binding.unlockC4.setTextColor(colorElementoRecurso)
            binding.unlockC5.setTextColor(colorElementoRecurso)
            binding.unlockC6.setTextColor(colorElementoRecurso)
        }

        //Cambiando a color de elemento oscuro a textos
        if (colorElementoDarkRecurso != null) {
            //Titulos
            binding.titleConstellations.setTextColor(colorElementoDarkRecurso)

            //Nombres Constelaciones
            binding.nameC1.setTextColor(colorElementoDarkRecurso)
            binding.nameC2.setTextColor(colorElementoDarkRecurso)
            binding.nameC3.setTextColor(colorElementoDarkRecurso)
            binding.nameC4.setTextColor(colorElementoDarkRecurso)
            binding.nameC5.setTextColor(colorElementoDarkRecurso)
            binding.nameC6.setTextColor(colorElementoDarkRecurso)
        }

        //Cambiando a color de elemento a cajas
        binding.boxC1.background = context?.let { ContextCompat.getDrawable(it,boxStyleElemento) }
        binding.boxC2.background = context?.let { ContextCompat.getDrawable(it,boxStyleElemento) }
        binding.boxC3.background = context?.let { ContextCompat.getDrawable(it,boxStyleElemento) }
        binding.boxC4.background = context?.let { ContextCompat.getDrawable(it,boxStyleElemento) }
        binding.boxC5.background = context?.let { ContextCompat.getDrawable(it,boxStyleElemento) }
        binding.boxC6.background = context?.let { ContextCompat.getDrawable(it,boxStyleElemento) }

    }

}