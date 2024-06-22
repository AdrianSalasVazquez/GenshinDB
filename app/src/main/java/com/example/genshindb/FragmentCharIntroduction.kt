package com.example.genshindb

import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.example.genshindb.databinding.FragmentCharIntroductionBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class FragmentCharIntroduction : Fragment() {

    private lateinit var binding: FragmentCharIntroductionBinding
    private lateinit var character: CharactersResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCharIntroductionBinding.inflate(layoutInflater)
        arguments?.let {
            character = it.getSerializable(ARG_PERSONAJE) as CharactersResponse
        }

        //Construccion del interfaz
        construccionInterfazIntroduccion()

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
        fun newInstance(personaje: CharactersResponse): FragmentCharIntroduction {
            val fragment = FragmentCharIntroduction()
            val args = Bundle()
            args.putSerializable(ARG_PERSONAJE, personaje)
            fragment.arguments = args
            return fragment
        }
    }


    private fun construccionInterfazIntroduccion() {
        //Dependiendo del elemento del personaje
        val colorElementoDark : Int
        val backgroundElemento : Int
        val boxStyleElemento : Int
        when (character.visionKey) {
            "ANEMO" -> {
                colorElementoDark = R.color.anemo_dark
                backgroundElemento = R.drawable.background_anemo
                boxStyleElemento = R.drawable.box_style_anemo
            }
            "CRYO" -> {
                colorElementoDark = R.color.cryo_dark
                backgroundElemento = R.drawable.background_cryo
                boxStyleElemento = R.drawable.box_style_cryo
            }
            "DENDRO" -> {
                colorElementoDark = R.color.dendro_dark
                backgroundElemento = R.drawable.background_dendro
                boxStyleElemento = R.drawable.box_style_dendro
            }
            "ELECTRO" -> {
                colorElementoDark = R.color.electro_dark
                backgroundElemento = R.drawable.background_electro
                boxStyleElemento = R.drawable.box_style_electro
            }
            "GEO" -> {
                colorElementoDark = R.color.geo_dark
                backgroundElemento = R.drawable.background_geo
                boxStyleElemento = R.drawable.box_style_geo
            }
            "HYDRO" -> {
                colorElementoDark = R.color.hydro_dark
                backgroundElemento = R.drawable.background_hydro
                boxStyleElemento = R.drawable.box_style_hydro
            }
            "PYRO" -> {
                colorElementoDark = R.color.pyro_dark
                backgroundElemento = R.drawable.background_pyro
                boxStyleElemento = R.drawable.box_style_pyro
            }
            else -> {
                colorElementoDark = 0
                backgroundElemento = 0
                boxStyleElemento = 0
            }
        }
        //Recuperando los recursos
        val colorElementoDarkRecurso = context?.let { ContextCompat.getColor(it, colorElementoDark) }
        val backgroundElementoRecurso = context?.let { ContextCompat.getDrawable(it,backgroundElemento) }

        // Cambio del background segun elemento
        binding.scrollIntroduccion.background = backgroundElementoRecurso

        //Traduciendo informacion de personaje
        val name : String
        val element: String
        val weapon : String
        val title: String
        val affiliation: String
        val nation = character.nation
        val constellation = character.constellation
        val description: String

        if (Locale.getDefault().displayLanguage.lowercase() == "español") {
            name = character.spanish.name
            weapon = character.spanish.weapon
            element = character.spanish.vision
            title = character.spanish.title
            affiliation = character.spanish.affiliation
            description = character.spanish.description
        }
        else{
            name = character.english.name
            weapon = character.english.weapon
            element = character.english.vision
            title = character.english.title
            affiliation = character.english.affiliation
            description = character.english.description
        }
        var rarity = ""
        for (i in character.rarity downTo 1) rarity = "$rarity★"

        //Formateo de cumpleaños
        val birthdaySplit = character.birthday.split("-")
        val dateFormat = SimpleDateFormat("d MMMM", Locale.getDefault())
        val birthday = dateFormat.format(Date(birthdaySplit[0].toInt(),birthdaySplit[1].toInt(),birthdaySplit[2].toInt()))

        // Carga de imagen Gacha Splash
        Picasso.get().load(character.resources.gachaSplashURL).into(binding.characterGachaSplash)

        //Cambiando colores en la caja de datos generales
        binding.generalInfoCharacterName.text = crearHintElemento(getString(R.string.name) + ":",name,colorElementoDarkRecurso)
        binding.generalInfoCharacterRarity.text = crearHintElemento(getString(R.string.rarity) + ":",rarity,colorElementoDarkRecurso)
        binding.generalInfoCharacterElement.text = crearHintElemento(getString(R.string.element) + ":",element,colorElementoDarkRecurso)
        binding.generalInfoCharacterWeapon.text = crearHintElemento(getString(R.string.weapon) + ":",weapon,colorElementoDarkRecurso)
        binding.generalInfoCharacterTitle.text = crearHintElemento(getString(R.string.title) + ":",title,colorElementoDarkRecurso)
        binding.generalInfoCharacterAffiliation.text = crearHintElemento(getString(R.string.affiliation) + ":",affiliation,colorElementoDarkRecurso)
        binding.generalInfoCharacterNation.text = crearHintElemento(getString(R.string.nation) + ":",nation,colorElementoDarkRecurso)
        binding.generalInfoCharacterConstellation.text = crearHintElemento(getString(R.string.constellation) + ":",constellation,colorElementoDarkRecurso)
        binding.generalInfoCharacterBirthday.text = crearHintElemento(getString(R.string.birthday) + ":",birthday,colorElementoDarkRecurso)
        binding.generalInfoCharacterDescription.text = crearHintElemento(getString(R.string.description) + ":",description,colorElementoDarkRecurso)


        //Prueba para calcular el listado de niveles

        //Creando listados de las estadísticas
        val listadoNiveles = arrayOf("1","20","20✦","40","40✦","50","50✦","60","60✦","70","70✦","80","80✦","90")
        val listadoVidaPorNivel = calcularListadoNiveles(character.stats.base_hp,character.rarity,character.stats.max_hp_value)
        val listadoAtaquePorNivel = calcularListadoNiveles(character.stats.base_atk,character.rarity,character.stats.max_atk_value)
        val listadoDefensaPorNivel = calcularListadoNiveles(character.stats.base_def,character.rarity,character.stats.max_def_value)

        val datosEstadisticas = mutableListOf<Array<String>>()
        for (nivel in listadoNiveles.indices) {
            val filaEstadisticas = arrayOf(listadoNiveles[nivel], listadoVidaPorNivel[nivel], listadoAtaquePorNivel[nivel], listadoDefensaPorNivel[nivel])
            datosEstadisticas.add(filaEstadisticas)
        }
        //Introduciendo los listados de estadisticas en la tabla
        for (filaEstadisticas in datosEstadisticas){
            val tableRow = TableRow(context)
            //Introducir cada estadistica de la fila
            for((iteracion, estadistica) in filaEstadisticas.withIndex()) {
                val textView = TextView(context)
                textView.text = estadistica
                textView.setPadding(5)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) textView.setTextAppearance(R.style.bodyTextType)
                textView.gravity = Gravity.CENTER
                if (iteracion == 0 && colorElementoDarkRecurso != null) textView.setTextColor(colorElementoDarkRecurso)
                tableRow.addView(textView)
            }
            binding.tableStatistics.addView(tableRow)
        }
        //Modificando head de la tabla
        if (colorElementoDarkRecurso != null) {
            binding.tableLevelHead.setTextColor(colorElementoDarkRecurso)
            binding.tableHealthHead.setTextColor(colorElementoDarkRecurso)
            binding.tableAttackHead.setTextColor(colorElementoDarkRecurso)
            binding.tableDefenseHead.setTextColor(colorElementoDarkRecurso)
        }

        //Cambiando a color de elemento a cajas de informacion
        binding.boxGeneralInfo.background = context?.let { ContextCompat.getDrawable(it,boxStyleElemento) }
        binding.boxStatistics.background = context?.let { ContextCompat.getDrawable(it,boxStyleElemento) }

        //Cambiando a color de elemento a titulos, subtitulos y separadores
        if (colorElementoDarkRecurso != null) {
            binding.titleGeneralInfo.setTextColor(colorElementoDarkRecurso)
            binding.titleStatistics.setTextColor(colorElementoDarkRecurso)
        }
    }




    //Cambiando a color de elemento a partes de datos
    private fun crearHintElemento(hint : String, texto : String, colorElemento : Int?): SpannableString {
        val spannableString = SpannableString("$hint $texto")
        if (colorElemento != null) spannableString.setSpan(ForegroundColorSpan(colorElemento), 0, hint.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    //CALCULAR ESTADISTICAS PERSONAJE
    private fun calcularEstadistica(estadisticaBase : Double, nivel : Int, rarity : Int, ascensionValue : Double) : Double {
        //Calculamos el multiplicador de nivel en funcion de la rareza del personaje
        val levelMultiplierList4Rarity = arrayOf(2.569,4.220,5.046,5.872,6.697,7.523,8.349)
        val levelMultiplierList5Rarity = arrayOf(2.594,4.307,5.176,6.054,6.940,7.836,8.739)
        val levelMultiplierListFinal : Array<Double> = if (rarity == 4) levelMultiplierList4Rarity else levelMultiplierList5Rarity
        val levelMultiplier = when (nivel){
            20 -> levelMultiplierListFinal[0]
            40 -> levelMultiplierListFinal[1]
            50 -> levelMultiplierListFinal[2]
            60 -> levelMultiplierListFinal[3]
            70 -> levelMultiplierListFinal[4]
            80 -> levelMultiplierListFinal[5]
            90 -> levelMultiplierListFinal[6]
            else -> 0.0
        }
        return estadisticaBase * levelMultiplier + ascensionValue
    }
    //Calculamos el valor de ascension del personaje
    private fun calcularValorAscension(seccionTotalAscension : Double, valorAscensionPersonaje : Double) : Double{
        return seccionTotalAscension * valorAscensionPersonaje
    }
    //Usando los metodos anteriores creamos un listado de Strings que usaremos para introducir en la tabla
    private fun calcularListadoNiveles(estadisticaBase: Double, rarity: Int, valorMaxEstadisticaPersonaje: Double): MutableList<String> {
        val listaNiveles = arrayOf("1","20","20✦","40","40✦","50","50✦","60","60✦","70","70✦","80","80✦","90")
        val listaResultados = mutableListOf<String>()
        for ((indice, nivel) in listaNiveles.withIndex()){
            val valorNivel = when (nivel) {
                "1" -> estadisticaBase
                "20" -> calcularEstadistica(estadisticaBase,20,rarity,calcularValorAscension(0.0,valorMaxEstadisticaPersonaje))
                "20✦" -> calcularEstadistica(estadisticaBase,20,rarity,calcularValorAscension(38/182.0,valorMaxEstadisticaPersonaje))
                "40" -> calcularEstadistica(estadisticaBase,40,rarity,calcularValorAscension(38/182.0,valorMaxEstadisticaPersonaje))
                "40✦" -> calcularEstadistica(estadisticaBase,40,rarity,calcularValorAscension(65/182.0,valorMaxEstadisticaPersonaje))
                "50" -> calcularEstadistica(estadisticaBase,50,rarity,calcularValorAscension(65/182.0,valorMaxEstadisticaPersonaje))
                "50✦" -> calcularEstadistica(estadisticaBase,50,rarity,calcularValorAscension(101/182.0,valorMaxEstadisticaPersonaje))
                "60" -> calcularEstadistica(estadisticaBase,60,rarity,calcularValorAscension(101/182.0,valorMaxEstadisticaPersonaje))
                "60✦" -> calcularEstadistica(estadisticaBase,60,rarity,calcularValorAscension(128/182.0,valorMaxEstadisticaPersonaje))
                "70" -> calcularEstadistica(estadisticaBase,70,rarity,calcularValorAscension(128/182.0,valorMaxEstadisticaPersonaje))
                "70✦" -> calcularEstadistica(estadisticaBase,70,rarity,calcularValorAscension(155/182.0,valorMaxEstadisticaPersonaje))
                "80" -> calcularEstadistica(estadisticaBase,80,rarity,calcularValorAscension(155/182.0,valorMaxEstadisticaPersonaje))
                "80✦" -> calcularEstadistica(estadisticaBase,80,rarity,calcularValorAscension(1.0,valorMaxEstadisticaPersonaje))
                "90" -> calcularEstadistica(estadisticaBase,90,rarity,calcularValorAscension(1.0,valorMaxEstadisticaPersonaje))
                else -> 0.0
            }.roundToInt().toString()
            listaResultados.add(indice,valorNivel)
        }
        return listaResultados
    }

}