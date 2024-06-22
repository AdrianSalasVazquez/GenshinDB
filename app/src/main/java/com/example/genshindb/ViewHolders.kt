package com.example.genshindb

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.genshindb.databinding.ItemCardBinding
import com.example.genshindb.databinding.ItemCharacterBinding
import com.squareup.picasso.Picasso
import java.util.*

// ViewHolder Personajes
class CharacterViewHolder(view: View, listener: CharacterAdapter.OnItemClickListener, context: Context):RecyclerView.ViewHolder(view) {

    private val binding = ItemCharacterBinding.bind(view)

    //Elementos
    private val anemoIcon = ContextCompat.getDrawable(context, R.drawable.element_anemo)
    private val cryoIcon = ContextCompat.getDrawable(context, R.drawable.element_cryo)
    private val dendroIcon = ContextCompat.getDrawable(context, R.drawable.element_dendro)
    private val electroIcon = ContextCompat.getDrawable(context, R.drawable.element_electro)
    private val geoIcon = ContextCompat.getDrawable(context, R.drawable.element_geo)
    private val hydroIcon = ContextCompat.getDrawable(context, R.drawable.element_hydro)
    private val pyroIcon = ContextCompat.getDrawable(context, R.drawable.element_pyro)
    //Tipos de Arma
    private val bowIcon = ContextCompat.getDrawable(context, R.drawable.icon_bow)
    private val catalystIcon = ContextCompat.getDrawable(context, R.drawable.icon_catalyst)
    private val claymoreIcon = ContextCompat.getDrawable(context, R.drawable.icon_claymore)
    private val polearmIcon = ContextCompat.getDrawable(context, R.drawable.icon_polearm)
    private val swordIcon = ContextCompat.getDrawable(context, R.drawable.icon_sword)
    //Naciones
    private val mondstadtIcon = "https://firebasestorage.googleapis.com/v0/b/genshindb-a57e6.appspot.com/o/iconos_naciones%2FMondstadt?alt=media&token=14cd1750-2c25-43fc-93b6-ecb8875b0287"
    private val liyueIcon = "https://firebasestorage.googleapis.com/v0/b/genshindb-a57e6.appspot.com/o/iconos_naciones%2FLiyue?alt=media&token=213045c1-8c29-447b-8df3-d47b1130ebf4"
    private val inazumaIcon = "https://firebasestorage.googleapis.com/v0/b/genshindb-a57e6.appspot.com/o/iconos_naciones%2FInazuma?alt=media&token=288cef1f-867b-4222-ab3d-af8c6c13e8de"
    private val sumeruIcon = "https://firebasestorage.googleapis.com/v0/b/genshindb-a57e6.appspot.com/o/iconos_naciones%2FSumeru?alt=media&token=6b1b70ed-81e0-484b-9db3-48e530385f8f"
    private val fontaineIcon = "https://firebasestorage.googleapis.com/v0/b/genshindb-a57e6.appspot.com/o/iconos_naciones%2FFontaine?alt=media&token=ecb4c407-078e-4cb0-8dde-ce35782a2d7d"
    private val uknownNationIcon = "https://firebasestorage.googleapis.com/v0/b/genshindb-a57e6.appspot.com/o/iconos_naciones%2FUknown?alt=media&token=5678d4e6-297c-4f24-83f7-095edab67561"

    //OnClickListener
    init {
        itemView.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }

    fun bind(character: CharactersResponse){

        //Idioma y textos
        var idioma = Locale.getDefault().displayLanguage.lowercase()
        if (idioma == "español"){
            //Cortar nombres largos
            if (character.spanish.name.count() > 18) {
                binding.characterName.text = character.spanish.name.substringAfterLast(" ").trim()
            }
            else binding.characterName.text = character.spanish.name
            binding.characterVision.text = character.spanish.vision
            binding.characterWeapon.text = character.spanish.weapon
        } else {
            //Cortar nombres largos
            if (character.english.name.count() > 18) {
                binding.characterName.text = character.english.name.substringAfterLast(" ").trim()
            }
            else binding.characterName.text = character.english.name
            binding.characterVision.text = character.english.vision
            binding.characterWeapon.text = character.english.weapon
        }

        //Icono Personaje
         if (character.resources.iconURL.isNotEmpty()) {
            Picasso.get().load(character.resources.iconURL).into(binding.characterIcon)
        }

        //Rareza
        var rarityText = ""
        for (i in character.rarity downTo 1) rarityText = "$rarityText★"
        binding.characterRarity.text = rarityText

        //Icono y color elemento personaje
        when (character.visionKey) {
            "ANEMO" -> {
                binding.characterCardView.setCardBackgroundColor(
                    android.graphics.Color.argb(
                        255,
                        1,
                        88,
                        74
                    )
                )
                binding.characterVisionIcon.setImageDrawable(anemoIcon)
            }
            "CRYO" -> {
                binding.characterCardView.setCardBackgroundColor(
                    android.graphics.Color.argb(
                        255,
                        0,
                        85,
                        106
                    )
                )
                binding.characterVisionIcon.setImageDrawable(cryoIcon)
            }
            "DENDRO" -> {
                binding.characterCardView.setCardBackgroundColor(
                    android.graphics.Color.argb(
                        255,
                        0,
                        79,
                        0
                    )
                )
                binding.characterVisionIcon.setImageDrawable(dendroIcon)
            }
            "ELECTRO" -> {
                binding.characterCardView.setCardBackgroundColor(
                    android.graphics.Color.argb(
                        255,
                        56,
                        0,
                        112
                    )
                )
                binding.characterVisionIcon.setImageDrawable(electroIcon)
            }
            "GEO" -> {
                binding.characterCardView.setCardBackgroundColor(
                    android.graphics.Color.argb(
                        255,
                        80,
                        48,
                        0
                    )
                )
                binding.characterVisionIcon.setImageDrawable(geoIcon)
            }
            "HYDRO" -> {
                binding.characterCardView.setCardBackgroundColor(
                    android.graphics.Color.argb(
                        255,
                        0,
                        44,
                        109
                    )
                )
                binding.characterVisionIcon.setImageDrawable(hydroIcon)
            }
            "PYRO" -> {
                binding.characterCardView.setCardBackgroundColor(
                    android.graphics.Color.argb(
                        255,
                        100,
                        14,
                        14
                    )
                )
                binding.characterVisionIcon.setImageDrawable(pyroIcon)
            }
        }

        //Icono arma personaje
        when (character.weaponType) {
            "BOW" -> binding.characterWeaponIcon.setImageDrawable(bowIcon)
            "CATALYST" -> binding.characterWeaponIcon.setImageDrawable(catalystIcon)
            "CLAYMORE" -> binding.characterWeaponIcon.setImageDrawable(claymoreIcon)
            "POLEARM" -> binding.characterWeaponIcon.setImageDrawable(polearmIcon)
            "SWORD" -> binding.characterWeaponIcon.setImageDrawable(swordIcon)
        }

        //Prueba imagen decorativa de las naciones
        when (character.nation.uppercase()){
            "MONDSTADT" -> Picasso.get().load(mondstadtIcon).into(binding.characterNation)
            "LIYUE" -> Picasso.get().load(liyueIcon).into(binding.characterNation)
            "INAZUMA" -> Picasso.get().load(inazumaIcon).into(binding.characterNation)
            "SUMERU" -> Picasso.get().load(sumeruIcon).into(binding.characterNation)
            "FONTAINE" -> Picasso.get().load(fontaineIcon).into(binding.characterNation)
            else -> Picasso.get().load(uknownNationIcon).into(binding.characterNation)
        }

        //Añadir degradado
        binding.characterLayout.setBackgroundResource(R.drawable.degradado)

    }
}

// ViewHolder Armas
class WeaponViewHolder(view: View, listener: WeaponAdapter.OnItemClickListener):RecyclerView.ViewHolder(view) {

    private val binding = ItemCardBinding.bind(view)

    //OnClickListener
    init {
        itemView.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }

    fun bind(weapon: WeaponsResponse){

        //Icono Arma
        if (weapon.resources.iconURL.isNotEmpty()) {
            Picasso.get().load(weapon.resources.iconURL).into(binding.itemIcon)
        }

        //Nombre Arma
        var idioma = Locale.getDefault().displayLanguage.lowercase()
        if (idioma == "español") binding.itemName.text = weapon.spanish.name
        else binding.itemName.text = weapon.english.name

        //Rareza
        var rarityText = ""
        for (i in weapon.rarity downTo 1) rarityText = "$rarityText★"
        binding.itemRarity.text = rarityText
        when (weapon.rarity){
            1 -> binding.itemIconLayout.setBackgroundResource(R.drawable.backgroundroundedrarity1)
            2 -> binding.itemIconLayout.setBackgroundResource(R.drawable.backgroundroundedrarity2)
            3 -> binding.itemIconLayout.setBackgroundResource(R.drawable.backgroundroundedrarity3)
            4 -> binding.itemIconLayout.setBackgroundResource(R.drawable.backgroundroundedrarity4)
            5 -> binding.itemIconLayout.setBackgroundResource(R.drawable.backgroundroundedrarity5)
        }

    }
}

// ViewHolder Artefactos
class ArtifactViewHolder(view: View, listener: ArtifactAdapter.OnItemClickListener):RecyclerView.ViewHolder(view) {

    private val binding = ItemCardBinding.bind(view)

    //OnClickListener
    init {
        itemView.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }

    fun bind(artifact: ArtifactsResponse){

        //Icono Artefacto
        if (artifact.resources.flowerIconURL.isNotEmpty()) {
            Picasso.get().load(artifact.resources.flowerIconURL).into(binding.itemIcon)
        }

        //Nombre Artefacto
        var idioma = Locale.getDefault().displayLanguage.lowercase()
        if (idioma == "español") binding.itemName.text = artifact.spanish.name
        else binding.itemName.text = artifact.english.name

        //Rareza Artefacto
        var rarityText = ""
        for (i in artifact.maxRarity downTo 1) rarityText = "$rarityText★"
        binding.itemRarity.text = rarityText
        when (artifact.maxRarity){
            1 -> binding.itemIconLayout.setBackgroundResource(R.drawable.backgroundroundedrarity1)
            2 -> binding.itemIconLayout.setBackgroundResource(R.drawable.backgroundroundedrarity2)
            3 -> binding.itemIconLayout.setBackgroundResource(R.drawable.backgroundroundedrarity3)
            4 -> binding.itemIconLayout.setBackgroundResource(R.drawable.backgroundroundedrarity4)
            5 -> binding.itemIconLayout.setBackgroundResource(R.drawable.backgroundroundedrarity5)
        }

    }
}