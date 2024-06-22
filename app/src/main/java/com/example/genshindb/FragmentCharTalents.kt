package com.example.genshindb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.genshindb.databinding.FragmentCharTalentsBinding
import com.squareup.picasso.Picasso
import java.util.*

class FragmentCharTalents : Fragment() {

    private lateinit var binding: FragmentCharTalentsBinding
    private lateinit var character: CharactersResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCharTalentsBinding.inflate(layoutInflater)
        arguments?.let {
            character = it.getSerializable(ARG_PERSONAJE) as CharactersResponse
        }

        //Construccion del interfaz
        construccionInterfazTalentos()

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
        fun newInstance(personaje: CharactersResponse): FragmentCharTalents {
            val fragment = FragmentCharTalents()
            val args = Bundle()
            args.putSerializable(ARG_PERSONAJE, personaje)
            fragment.arguments = args
            return fragment
        }
    }

    private fun construccionInterfazTalentos() {

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
        binding.scrollTalentos.background = backgroundElementoRecurso

        // Cambio del background segun elemento en icono de habilidad
        binding.backgroundIconNormalAtk.background = backgroundElementoRecursoIcon
        binding.backgroundIconElementalSkill.background = backgroundElementoRecursoIcon
        binding.backgroundIconElementalBurst.background = backgroundElementoRecursoIcon
        // Cambio del background segun elemento en icono de pasiva
        binding.backgroundIconPassive1.background = backgroundElementoRecursoIcon
        binding.backgroundIconPassive2.background = backgroundElementoRecursoIcon
        binding.backgroundIconPassiveBasic.background = backgroundElementoRecursoIcon

        //Añadir iconos de las habilidades
        Picasso.get().load(character.resources.iconTalents.iconAttackURL).into(binding.iconNormalAtk)
        Picasso.get().load(character.resources.iconTalents.iconSkillURL).into(binding.iconElementalSkill)
        Picasso.get().load(character.resources.iconTalents.iconBurstURL).into(binding.iconElementalBurst)
        //Añadir iconos de las pasivas
        Picasso.get().load(character.resources.iconTalents.iconPassive1URL).into(binding.iconPassive1)
        Picasso.get().load(character.resources.iconTalents.iconPassive2URL).into(binding.iconPassive2)
        Picasso.get().load(character.resources.iconTalents.iconPassiveBasicURL).into(binding.iconPassiveBasic)

        //Introduccion de textos por idioma
        if (Locale.getDefault().displayLanguage.lowercase() == "español") {
            //Ataque normal
            binding.nameNormalAtk.text = character.spanish.skillTalents.normal_attack.name
            binding.unlockNormalAtk.text = character.spanish.skillTalents.normal_attack.unlock
            binding.descNormalAtk.text = character.spanish.skillTalents.normal_attack.description
            //Habilidad elemental
            binding.nameElementalSkill.text = character.spanish.skillTalents.elemental_skill.name
            binding.unlockElementalSkill.text = character.spanish.skillTalents.elemental_skill.unlock
            binding.descElementalSkill.text = character.spanish.skillTalents.elemental_skill.description
            //Habilidad definitiva
            binding.nameElementalBurst.text = character.spanish.skillTalents.elemental_burst.name
            binding.unlockElementalBurst.text = character.spanish.skillTalents.elemental_burst.unlock
            binding.descElementalBurst.text = character.spanish.skillTalents.elemental_burst.description
            //Pasiva 1
            binding.namePassive1.text = character.spanish.passiveTalents.passive_1.name
            binding.unlockPassive1.text = character.spanish.passiveTalents.passive_1.unlock
            binding.descPassive1.text = character.spanish.passiveTalents.passive_1.description
            //Pasiva 2
            binding.namePassive2.text = character.spanish.passiveTalents.passive_2.name
            binding.unlockPassive2.text = character.spanish.passiveTalents.passive_2.unlock
            binding.descPassive2.text = character.spanish.passiveTalents.passive_2.description
            //Pasiva Basica
            binding.namePassiveBasic.text = character.spanish.passiveTalents.passive_basic.name
            binding.unlockPassiveBasic.text = character.spanish.passiveTalents.passive_basic.unlock
            binding.descPassiveBasic.text = character.spanish.passiveTalents.passive_basic.description
        }
        else {
            //Ataque normal
            binding.nameNormalAtk.text = character.english.skillTalents.normal_attack.name
            binding.unlockNormalAtk.text = character.english.skillTalents.normal_attack.unlock
            binding.descNormalAtk.text = character.english.skillTalents.normal_attack.description
            //Habilidad elemental
            binding.nameElementalSkill.text = character.english.skillTalents.elemental_skill.name
            binding.unlockElementalSkill.text = character.english.skillTalents.elemental_skill.unlock
            binding.descElementalSkill.text = character.english.skillTalents.elemental_skill.description
            //Habilidad definitiva
            binding.nameElementalBurst.text = character.english.skillTalents.elemental_burst.name
            binding.unlockElementalBurst.text = character.english.skillTalents.elemental_burst.unlock
            binding.descElementalBurst.text = character.english.skillTalents.elemental_burst.description
            //Pasiva 1
            binding.namePassive1.text = character.english.passiveTalents.passive_1.name
            binding.unlockPassive1.text = character.english.passiveTalents.passive_1.unlock
            binding.descPassive1.text = character.english.passiveTalents.passive_1.description
            //Pasiva 2
            binding.namePassive2.text = character.english.passiveTalents.passive_2.name
            binding.unlockPassive2.text = character.english.passiveTalents.passive_2.unlock
            binding.descPassive2.text = character.english.passiveTalents.passive_2.description
            //Pasiva Basica
            binding.namePassiveBasic.text = character.english.passiveTalents.passive_basic.name
            binding.unlockPassiveBasic.text = character.english.passiveTalents.passive_basic.unlock
            binding.descPassiveBasic.text = character.english.passiveTalents.passive_basic.description
        }

        //Cambiando a color de elemento a textos
        if (colorElementoRecurso != null) {
            binding.unlockNormalAtk.setTextColor(colorElementoRecurso)
            binding.unlockElementalSkill.setTextColor(colorElementoRecurso)
            binding.unlockElementalBurst.setTextColor(colorElementoRecurso)

            binding.unlockPassive1.setTextColor(colorElementoRecurso)
            binding.unlockPassive2.setTextColor(colorElementoRecurso)
            binding.unlockPassiveBasic.setTextColor(colorElementoRecurso)
        }
        //Cambiando a color de elemento oscuro a textos
        if (colorElementoDarkRecurso != null) {
            //Titulos
            binding.titleSkills.setTextColor(colorElementoDarkRecurso)
            binding.titlePassives.setTextColor(colorElementoDarkRecurso)

            //Nombre habilidades
            binding.nameNormalAtk.setTextColor(colorElementoDarkRecurso)
            binding.nameElementalSkill.setTextColor(colorElementoDarkRecurso)
            binding.nameElementalBurst.setTextColor(colorElementoDarkRecurso)
            //Nombre pasivas
            binding.namePassive1.setTextColor(colorElementoDarkRecurso)
            binding.namePassive2.setTextColor(colorElementoDarkRecurso)
            binding.namePassiveBasic.setTextColor(colorElementoDarkRecurso)
        }

        //Cambiando a color de elemento a cajas
        binding.boxNormalAtk.background = context?.let { ContextCompat.getDrawable(it,boxStyleElemento) }
        binding.boxElementalSkill.background = context?.let { ContextCompat.getDrawable(it,boxStyleElemento) }
        binding.boxElementalBurst.background = context?.let { ContextCompat.getDrawable(it,boxStyleElemento) }

        binding.boxPassive1.background = context?.let { ContextCompat.getDrawable(it,boxStyleElemento) }
        binding.boxPassive2.background = context?.let { ContextCompat.getDrawable(it,boxStyleElemento) }
        binding.boxPassiveBasic.background = context?.let { ContextCompat.getDrawable(it,boxStyleElemento) }
    }

}