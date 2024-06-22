package com.example.genshindb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.genshindb.databinding.ActivityCharacterDescBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class CharacterDescActivity : AppCompatActivity() {

    //Binding
    private lateinit var binding: ActivityCharacterDescBinding

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterDescBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //ActionBar
        setSupportActionBar(binding.characterToolBar)
        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Recreación del objeto personaje
        val character : CharactersResponse = intent.getSerializableExtra("character") as CharactersResponse

        //Creacion del menu superior a base del personaje
        pintarMenuSuperior(character)

        //Creacion de la funcionalidad del menu superior
        crearTabNavigation(character)

    }

    private fun crearTabNavigation(character: CharactersResponse) {
        //Carga de fragmentos con el personaje
        val fragmentoIntroduccion = FragmentCharIntroduction.newInstance(character)
        val fragmentoTalentos = FragmentCharTalents.newInstance(character)
        val fragmentoConstelacion = FragmentCharConstellation.newInstance(character)
        val fragmentoConstruccion = FragmentCharBuild.newInstance(character)

        //Cracion del PageAdapter y añadir fragmentos cargados con el personaje
        val pageAdapter = ViewPagerAdapter(this)
        pageAdapter.addFragment(fragmentoIntroduccion,resources.getString(R.string.introduction))
        pageAdapter.addFragment(fragmentoTalentos,resources.getString(R.string.talents))
        pageAdapter.addFragment(fragmentoConstelacion,resources.getString(R.string.constellation))
        pageAdapter.addFragment(fragmentoConstruccion,resources.getString(R.string.build))

        // Vincular el adaptador tanto en el ViewPager como en las tab
        binding.characterViewpager.adapter = pageAdapter
        TabLayoutMediator(binding.characterDescTab, binding.characterViewpager) { tab, position ->
            tab.text = pageAdapter.mFragmentList[position].second
        }.attach()
    }

    private fun pintarMenuSuperior(character: CharactersResponse) {
        //Nombre personaje
        val idioma = Locale.getDefault().displayLanguage.lowercase()
        if (idioma == "español") {
            binding.actionbarTitle.text = character.spanish.name
        }
        else {
            binding.actionbarTitle.text = character.english.name
        }

        //Elemento personaje
        when (character.visionKey) {
            "ANEMO" ->  {
                binding.actionbarIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.element_anemo))
                binding.characterDescTab.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.anemo))
                binding.characterDescTab.setTabRippleColorResource(R.color.anemo_dark)
            }
            "CRYO" ->  {
                binding.actionbarIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.element_cryo))
                binding.characterDescTab.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.cryo))
                binding.characterDescTab.setTabRippleColorResource(R.color.cryo_dark)
            }
            "DENDRO" ->  {
                binding.actionbarIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.element_dendro))
                binding.characterDescTab.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.dendro))
                binding.characterDescTab.setTabRippleColorResource(R.color.dendro_dark)
            }
            "ELECTRO" ->  {
                binding.actionbarIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.element_electro))
                binding.characterDescTab.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.electro))
                binding.characterDescTab.setTabRippleColorResource(R.color.electro_dark)
            }
            "GEO" ->  {
                binding.actionbarIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.element_geo))
                binding.characterDescTab.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.geo))
                binding.characterDescTab.setTabRippleColorResource(R.color.geo_dark)
            }
            "HYDRO" ->  {
                binding.actionbarIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.element_hydro))
                binding.characterDescTab.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.hydro))
                binding.characterDescTab.setTabRippleColorResource(R.color.hydro_dark)
            }
            "PYRO" ->  {
                binding.actionbarIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.element_pyro))
                binding.characterDescTab.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.pyro))
                binding.characterDescTab.setTabRippleColorResource(R.color.pyro_dark)
            }
        }

    }
}
