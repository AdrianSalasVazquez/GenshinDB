package com.example.genshindb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.genshindb.databinding.ActivityArtifactDescBinding
import com.squareup.picasso.Picasso
import java.util.Locale

class ArtifactDescActivity : AppCompatActivity() {

    //Binding
    private lateinit var binding: ActivityArtifactDescBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtifactDescBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //ActionBar
        setSupportActionBar(binding.artifactToolBar)
        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Recuperacion del objeto artefacto
        val artifact: ArtifactsResponse = intent.getSerializableExtra("artifact") as ArtifactsResponse

        //Creacion de la interfaz
        crearInterfazArtefacto(artifact)

    }

    private fun crearInterfazArtefacto(artifact: ArtifactsResponse) {

        //Buscar el idioma del dispositivo
        var idioma = Locale.getDefault().displayLanguage.lowercase()

        //Titulo artefacto
        if (idioma == "español") binding.actionbarTitle.text = artifact.spanish.name
        else binding.actionbarTitle.text = artifact.english.name

        //Cambiar recursos en funcion de la rareza
        var colorRareza : Int
        var boxStyleRareza : Int
        when (artifact.maxRarity){
            1 -> {
                binding.scrollArtifact.setBackgroundResource(R.drawable.backgroundrarity1)
                boxStyleRareza = R.drawable.box_style_rarity1
                colorRareza = R.color.rareza1
            }
            2 -> {
                binding.scrollArtifact.setBackgroundResource(R.drawable.backgroundrarity2)
                boxStyleRareza = R.drawable.box_style_rarity2
                colorRareza = R.color.rareza2
            }
            3 -> {
                binding.scrollArtifact.setBackgroundResource(R.drawable.backgroundrarity3)
                boxStyleRareza = R.drawable.box_style_rarity3
                colorRareza = R.color.rareza3
            }
            4 -> {
                binding.scrollArtifact.setBackgroundResource(R.drawable.backgroundrarity4)
                boxStyleRareza = R.drawable.box_style_rarity4
                colorRareza = R.color.rareza4
            }
            5 -> {
                binding.scrollArtifact.setBackgroundResource(R.drawable.backgroundrarity5)
                boxStyleRareza = R.drawable.box_style_rarity5
                colorRareza = R.color.rareza5
            }
            else -> {
                boxStyleRareza = 0
                colorRareza = 0
            }
        }

        //Cambiar colores a cajas de texto
        if (boxStyleRareza != 0) binding.boxArtifactBonus.background = ContextCompat.getDrawable(this,boxStyleRareza)

        //Cambiar colores a titulos
        if (colorRareza != 0) {
            binding.titleArtifactSet.setTextColor(ContextCompat.getColor(this,colorRareza))
            binding.titleArtifactBonus.setTextColor(ContextCompat.getColor(this,colorRareza))

            binding.subtitle2Pieces.setTextColor(ContextCompat.getColor(this,colorRareza))
            binding.subtitle4Pieces.setTextColor(ContextCompat.getColor(this,colorRareza))
        }

        //Introducir los iconos del set de artefactos en los imageViews
        //Flor
        if (artifact.resources.flowerIconURL != null) Picasso.get().load(artifact.resources.flowerIconURL).into(binding.iconArtifactFlower)
        //Pluma
        if (artifact.resources.plumeIconURL != null) Picasso.get().load(artifact.resources.plumeIconURL).into(binding.iconArtifactPlume)
        //Reloj
        if (artifact.resources.sandsIconURL != null) Picasso.get().load(artifact.resources.sandsIconURL).into(binding.iconArtifactSands)
        //Caliz
        if (artifact.resources.gobletIconURL != null) Picasso.get().load(artifact.resources.gobletIconURL).into(binding.iconArtifactGoblet)
        //Casco
        if (artifact.resources.circletIconURL != null) Picasso.get().load(artifact.resources.circletIconURL).into(binding.iconArtifactCirclet)

        //Añadir textos a la descripcion de Bono artefactos
        if (idioma == "español") {
            binding.description2Pieces.text = artifact.spanish.pieceBonus2
            binding.description4Pieces.text = artifact.spanish.pieceBonus4
        }
        else {
            binding.description2Pieces.text = artifact.english.pieceBonus2
            binding.description4Pieces.text = artifact.english.pieceBonus4
        }

    }
}