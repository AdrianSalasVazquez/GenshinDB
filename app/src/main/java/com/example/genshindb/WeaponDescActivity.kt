package com.example.genshindb

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.genshindb.databinding.ActivityWeaponDescBinding
import com.squareup.picasso.Picasso
import java.util.Locale

class WeaponDescActivity : AppCompatActivity() {

    //Binding
    private lateinit var binding: ActivityWeaponDescBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeaponDescBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //ActionBar
        setSupportActionBar(binding.weaponToolBar)
        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Recuperacion del objeto arma
        val weapon: WeaponsResponse = intent.getSerializableExtra("weapon") as WeaponsResponse

        //Creacion de la interfaz
        crearInterfazArma(weapon)
    }

    private fun crearInterfazArma(weapon: WeaponsResponse) {

        //Titulo arma
        val idioma = Locale.getDefault().displayLanguage.lowercase()
        if (idioma == "español") binding.actionbarTitle.text = weapon.spanish.name
        else binding.actionbarTitle.text = weapon.english.name

        //Cambiar icono en funcion al tipo de arma
        when (weapon.type){
            "Bow" -> binding.actionbarIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_bow))
            "Catalyst" -> binding.actionbarIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_catalyst))
            "Claymore" -> binding.actionbarIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_claymore))
            "Polearm" -> binding.actionbarIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_polearm))
            "Sword" -> binding.actionbarIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_sword))
        }

        //Cambiar recursos en funcion de la rareza
        val colorRareza : Int
        val boxStyleRareza : Int
        when (weapon.rarity){
            1 -> {
                binding.scrollWeapon.setBackgroundResource(R.drawable.backgroundrarity1)
                boxStyleRareza = R.drawable.box_style_rarity1
                colorRareza = R.color.rareza1
            }
            2 -> {
                binding.scrollWeapon.setBackgroundResource(R.drawable.backgroundrarity2)
                boxStyleRareza = R.drawable.box_style_rarity2
                colorRareza = R.color.rareza2
            }
            3 -> {
                binding.scrollWeapon.setBackgroundResource(R.drawable.backgroundrarity3)
                boxStyleRareza = R.drawable.box_style_rarity3
                colorRareza = R.color.rareza3
            }
            4 -> {
                binding.scrollWeapon.setBackgroundResource(R.drawable.backgroundrarity4)
                boxStyleRareza = R.drawable.box_style_rarity4
                colorRareza = R.color.rareza4
            }
            5 -> {
                binding.scrollWeapon.setBackgroundResource(R.drawable.backgroundrarity5)
                boxStyleRareza = R.drawable.box_style_rarity5
                colorRareza = R.color.rareza5
            }
            else -> {
                boxStyleRareza = 0
                colorRareza = 0
            }
        }
        //Convertir colotRareza en recurso para acceder a el directamente
        val recursoColorRareza = ContextCompat.getColor(this,colorRareza)


        //Cambiar colores a cajas de texto
        if (boxStyleRareza != 0){
            binding.boxGeneralInfoWeapon.background = ContextCompat.getDrawable(this,boxStyleRareza)
            binding.boxPassiveWeaponGeneral.background = ContextCompat.getDrawable(this,boxStyleRareza)
        }

        //Cambiar colores a titulos
        binding.titleGeneralInfoWeapon.setTextColor(recursoColorRareza)
        binding.titlePassiveWeapon.setTextColor(recursoColorRareza)
        binding.titleRefinement.setTextColor(recursoColorRareza)
        binding.nameWeaponPassive.setTextColor(recursoColorRareza)

        //Añadir Gacha Splash del arma con rotacion si no es Catalizador
        if (weapon.resources.gachaIconURL != null) {
            val imagenArma = Picasso.get().load(weapon.resources.gachaIconURL)
            if (weapon.type != "Catalyst") binding.weaponGachaSplash.rotation = 40f
            else {
                imagenArma.resize(400,0)
                binding.weaponGachaSplash.rotation = 20f
            }
            imagenArma.into(binding.weaponGachaSplash)
        }

        //Añadir hints a la caja de texto de informacion general del arma
        var rarityText = ""
        for (i in weapon.rarity downTo 1) rarityText = "$rarityText★"
        if (idioma == "español") {
            val tipoArmaEspaniol = when (weapon.type) {
                "Bow" -> "Arco"
                "Catalyst" -> "Catalizador"
                "Claymore" -> "Mandoble"
                "Polearm" -> "Lanza"
                "Sword" -> "Espada Ligera"
                else -> ""
            }
            binding.generalInfoWeaponName.text = crearHintElemento(getString(R.string.name) + ":",weapon.spanish.name,recursoColorRareza)
            binding.generalInfoWeaponType.text = crearHintElemento(getString(R.string.weapon_type) + ":",tipoArmaEspaniol,recursoColorRareza)
            binding.generalInfoWeaponDesc.text = crearHintElemento(getString(R.string.description) + ":",weapon.spanish.description,recursoColorRareza)
            binding.generalInfoWeaponObtaining.text = crearHintElemento(getString(R.string.obtaining) + ":",weapon.spanish.location,recursoColorRareza)
        }
        else {
            binding.generalInfoWeaponName.text = crearHintElemento(getString(R.string.name) + ":",weapon.english.name,recursoColorRareza)
            binding.generalInfoWeaponType.text = crearHintElemento(getString(R.string.weapon_type) + ":",weapon.type,recursoColorRareza)
            binding.generalInfoWeaponDesc.text = crearHintElemento(getString(R.string.description) + ":",weapon.english.description,recursoColorRareza)
            binding.generalInfoWeaponObtaining.text = crearHintElemento(getString(R.string.obtaining) + ":",weapon.english.location,recursoColorRareza)
        }
        binding.generalInfoWeaponRarity.text = crearHintElemento(getString(R.string.rarity) + ":",rarityText,recursoColorRareza)

        //Inicializar refinamientos
        binding.titleRefinement.text = getString(R.string.refinement,1)
        if (idioma == "español") {
            binding.nameWeaponPassive.text = weapon.spanish.passiveName + ":"
            binding.descWeaponPassiveRefinement.text = crearTextoRefinamiento(weapon.spanish.passiveDesc,recursoColorRareza,1)
        }
        else {
            binding.nameWeaponPassive.text = weapon.english.passiveName + ":"
            binding.descWeaponPassiveRefinement.text = crearTextoRefinamiento(weapon.english.passiveDesc,recursoColorRareza,1)
        }


        //Cambiar color de la SeekBar al color de la rareza
        binding.seekBarRefinement.progressTintList =  ColorStateList.valueOf(recursoColorRareza)
        binding.seekBarRefinement.thumbTintList =  ColorStateList.valueOf(recursoColorRareza)

        //Refinamientos de arma
        binding.seekBarRefinement.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            //Cuando la seekbar realiza cambios
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.titleRefinement.text = getString(R.string.refinement,progress)
                if (idioma == "español") binding.descWeaponPassiveRefinement.text = crearTextoRefinamiento(weapon.spanish.passiveDesc,recursoColorRareza,progress)
                else binding.descWeaponPassiveRefinement.text = crearTextoRefinamiento(weapon.english.passiveDesc,recursoColorRareza,progress)

            }
        })

    }

    //Indicando el refinamiento saca el texto correspondiente
    private fun crearTextoRefinamiento(texto : String, colorElemento : Int?, numRefinamiento : Int): SpannableString {
        val patronBusqueda = Regex("\\d+(\\.\\d+)?(\\,\\d+)?/\\d+(\\.\\d+)?(\\,\\d+)?/\\d+(\\.\\d+)?(\\,\\d+)?/\\d+(\\.\\d+)?(\\,\\d+)?/\\d+(\\.\\d+)?(\\,\\d+)?")
        var textoCambio = texto
        var textoCambioSpannable = SpannableString("")
        var listadoCoincidencias: MutableList<MatchResult> = mutableListOf()
        var index = 0

        //Por cada coincidencia me cambia al texto de el numero de refinamiento
        do {
            val coincidencia = patronBusqueda.find(textoCambio)
            if (coincidencia?.value != null) {
                listadoCoincidencias.add(index,coincidencia)
                val listadoNumCoindicendia = coincidencia.value.split("/")
                textoCambio = textoCambio.replaceRange(coincidencia.range.first, coincidencia.range.last+1, listadoNumCoindicendia[numRefinamiento-1])
                index++
            }
        } while (coincidencia?.value != null)

        //Por cada coincidencia pinta el texto del cambio
        textoCambioSpannable = SpannableString(textoCambio)
        for (coincidencia in listadoCoincidencias) {
            val listadoNumCoindicendia = coincidencia.value.split("/")
            if (colorElemento != null)
                textoCambioSpannable.setSpan(ForegroundColorSpan(colorElemento), coincidencia.range.first, coincidencia.range.first + listadoNumCoindicendia[numRefinamiento-1].length + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        return textoCambioSpannable
    }

    //Cambiando a color de elemento a partes de datos
    private fun crearHintElemento(hint : String, texto : String, colorElemento : Int?): SpannableString {
        val spannableString = SpannableString("$hint $texto")
        if (colorElemento != null) spannableString.setSpan(ForegroundColorSpan(colorElemento), 0, hint.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }

}