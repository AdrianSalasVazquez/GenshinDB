package com.example.genshindb

import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.SearchView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.genshindb.databinding.DialogFiltersCharactersBinding
import com.example.genshindb.databinding.FragmentCharactersBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException
import java.util.*

class CharactersFragment : Fragment() {

    private lateinit var binding: FragmentCharactersBinding
    private lateinit var adapter: CharacterAdapter
    private val utiles: MainActivity = MainActivity()
    private val allCharacterList = mutableListOf<CharactersResponse>()
    private val characterList = mutableListOf<CharactersResponse>()
    private val termination = ".json?print=pretty"
    private var isFilterDialogOpen = false

    //Variables filtros
    private var filterRarity = 0
    private var filterPyro = true
    private var filterHydro = true
    private var filterAnemo = true
    private var filterElectro = true
    private var filterDendro = true
    private var filterCryo = true
    private var filterGeo = true
    private var filterSword = true
    private var filterClaymore = true
    private var filterPolearm = true
    private var filterCatalyst = true
    private var filterBow = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCharactersBinding.inflate(layoutInflater)

        //Iniciar ReciclerView y busqueda inicial
        iniciarRecyclerView(characterList)
        buscarTodosPersonajes()



        //Buscador
        inicializarBuscador()

        //Filtros
        binding.btnFilterCharacters.setOnClickListener{
            if (!isFilterDialogOpen) crearVentanaFiltros()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        fun newInstance(): CharactersFragment = CharactersFragment()
    }

    private fun iniciarRecyclerView(list : MutableList<CharactersResponse>) {

        // Calcular el número de columnas dinámicamente
        val screenWidth = resources.displayMetrics.widthPixels
        val desiredColumnWidthDp = 380 // Ancho deseado por columna en dp
        var spanCount = screenWidth / (desiredColumnWidthDp * resources.displayMetrics.density).toInt()
        if (spanCount < 1) spanCount = 1

        //Añadir lista al adaptador, añadir el gridLayoutManager y añadirlo al recyclerview
        adapter = CharacterAdapter(list)
        binding.rvCharacters.layoutManager = GridLayoutManager(context, spanCount)
        binding.rvCharacters.adapter = adapter

        //OnItemClickListener
        adapter.setOnItemClickListener(object : CharacterAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                //Crear activity del personaje y enviar el objeto personaje
                val intent = Intent(context, CharacterDescActivity::class.java)
                intent.putExtra("character",list[position])
                startActivity(intent)
            }
        })
        //OnItemClickListener
    }

    private fun buscarTodosPersonajes() {
        CoroutineScope(Dispatchers.IO).launch {

            try {

                val call = utiles.getRetrofit().create(APIservice::class.java).getCharacters("characters"+ termination)
                val characters = call.body()
                activity?.runOnUiThread {
                    if (call.isSuccessful) {
                        val tryCharacters = characters ?: emptyList()
                        allCharacterList.addAll(tryCharacters)
                        characterList.addAll(allCharacterList)
                        adapter.notifyDataSetChanged()
                    } else {
                        if (Locale.getDefault().displayLanguage.lowercase() == "español") Toast.makeText(context, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
                        else Toast.makeText(context, "An error has occurred", Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: UnknownHostException) {
                e.printStackTrace()
                println("Unknown host: ${e.message}")
            } catch (e: IOException) {
                e.printStackTrace()
                println("Network error: ${e.message}")
            } catch (e: HttpException) {
                e.printStackTrace()
                println("HTTP error: ${e.message}")
            } catch (e: Exception) {
                e.printStackTrace()
                println("Unexpected error: ${e.message}")
            }

        }
    }

    private fun inicializarBuscador() {
        binding.svCharacters.clearFocus()
        binding.svCharacters.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.tvInformativoCharacters.text = ""
                filtrarListaBusqueda(newText)
                return true
            }

        })
    }
    private fun filtrarListaBusqueda(query : String?){
        if (!query.isNullOrEmpty()) {
            val listaBuscada = mutableListOf<CharactersResponse>()
            var texto = ""
            for (character in allCharacterList) {
                texto = if (Locale.getDefault().displayLanguage.lowercase() == "español"){
                    if (character.spanish.name.lowercase().contains(query.lowercase())) listaBuscada.add(character)
                    "No se ha encontrado ningun personaje"
                } else {
                    if (character.english.name.lowercase().contains(query.lowercase())) listaBuscada.add(character)
                    "No character was found"
                }
            }
            characterList.clear()
            characterList.addAll(listaBuscada)

            if (listaBuscada.isEmpty()) binding.tvInformativoCharacters.text = texto
        } else {
            characterList.clear()
            characterList.addAll(allCharacterList)
        }
        filtrarListaRareza()
        filtrarListaElementos()
        filtrarListaTipoArma()
        adapter.notifyDataSetChanged()
    }

    private fun crearVentanaFiltros() {
        val bindingDialog = DialogFiltersCharactersBinding.inflate(layoutInflater)
        val dialog = context?.let { Dialog(it) }
        if (dialog != null) {
            isFilterDialogOpen = true
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(bindingDialog.root)

            //RatingBar Rareza
            bindingDialog.ratingBarRarity.setOnRatingBarChangeListener { _, rating, _ ->
                filterRarity = rating.toInt()
                filtrarListaBusqueda(binding.svCharacters.query.toString())
            }

            //Onclick elementos
            bindingDialog.btnPyro.setOnCheckedChangeListener { _, _ ->
                filterPyro = onToggleFilterChange(bindingDialog.btnPyro)
                filtrarListaBusqueda(binding.svCharacters.query.toString())
            }
            bindingDialog.btnHydro.setOnCheckedChangeListener { _, _ ->
                filterHydro = onToggleFilterChange(bindingDialog.btnHydro)
                filtrarListaBusqueda(binding.svCharacters.query.toString())
            }
            bindingDialog.btnAnemo.setOnCheckedChangeListener { _, _ ->
                filterAnemo = onToggleFilterChange(bindingDialog.btnAnemo)
                filtrarListaBusqueda(binding.svCharacters.query.toString())
            }
            bindingDialog.btnElectro.setOnCheckedChangeListener { _, _ ->
                filterElectro = onToggleFilterChange(bindingDialog.btnElectro)
                filtrarListaBusqueda(binding.svCharacters.query.toString())
            }
            bindingDialog.btnDendro.setOnCheckedChangeListener { _, _ ->
                filterDendro = onToggleFilterChange(bindingDialog.btnDendro)
                filtrarListaBusqueda(binding.svCharacters.query.toString())
            }
            bindingDialog.btnCryo.setOnCheckedChangeListener { _, _ ->
                filterCryo = onToggleFilterChange(bindingDialog.btnCryo)
                filtrarListaBusqueda(binding.svCharacters.query.toString())
            }
            bindingDialog.btnGeo.setOnCheckedChangeListener { _, _ ->
                filterGeo = onToggleFilterChange(bindingDialog.btnGeo)
                filtrarListaBusqueda(binding.svCharacters.query.toString())
            }

            //Onclick tipo de arma
            bindingDialog.btnSword.setOnCheckedChangeListener { _, _ ->
                filterSword = onToggleFilterChange(bindingDialog.btnSword)
                filtrarListaBusqueda(binding.svCharacters.query.toString())
            }
            bindingDialog.btnClaymore.setOnCheckedChangeListener { _, _ ->
                filterClaymore = onToggleFilterChange(bindingDialog.btnClaymore)
                filtrarListaBusqueda(binding.svCharacters.query.toString())
            }
            bindingDialog.btnPolearm.setOnCheckedChangeListener { _, _ ->
                filterPolearm = onToggleFilterChange(bindingDialog.btnPolearm)
                filtrarListaBusqueda(binding.svCharacters.query.toString())
            }
            bindingDialog.btnCatalyst.setOnCheckedChangeListener { _, _ ->
                filterCatalyst = onToggleFilterChange(bindingDialog.btnCatalyst)
                filtrarListaBusqueda(binding.svCharacters.query.toString())
            }
            bindingDialog.btnBow.setOnCheckedChangeListener { _, _ ->
                filterBow = onToggleFilterChange(bindingDialog.btnBow)
                filtrarListaBusqueda(binding.svCharacters.query.toString())
            }

            //Onclick Boton limpiar filtros
            bindingDialog.btnClearFilters.setOnClickListener{
                filterRarity = 0
                filterPyro = true
                filterHydro = true
                filterAnemo = true
                filterElectro = true
                filterDendro = true
                filterCryo = true
                filterGeo = true
                filterSword = true
                filterClaymore = true
                filterPolearm = true
                filterCatalyst = true
                filterBow = true
                bindingDialog.ratingBarRarity.rating = 0F
                bindingDialog.btnPyro.isChecked = true
                bindingDialog.btnHydro.isChecked = true
                bindingDialog.btnAnemo.isChecked = true
                bindingDialog.btnElectro.isChecked = true
                bindingDialog.btnDendro.isChecked = true
                bindingDialog.btnCryo.isChecked = true
                bindingDialog.btnGeo.isChecked = true
                bindingDialog.btnSword.isChecked = true
                bindingDialog.btnClaymore.isChecked = true
                bindingDialog.btnPolearm.isChecked = true
                bindingDialog.btnCatalyst.isChecked = true
                bindingDialog.btnBow.isChecked = true
                filtrarListaBusqueda(binding.svCharacters.query.toString())
            }

            //Inicializar Botones
            bindingDialog.ratingBarRarity.rating = filterRarity.toFloat()
            bindingDialog.btnPyro.isChecked = filterPyro
            bindingDialog.btnHydro.isChecked = filterHydro
            bindingDialog.btnAnemo.isChecked = filterAnemo
            bindingDialog.btnElectro.isChecked = filterElectro
            bindingDialog.btnDendro.isChecked = filterDendro
            bindingDialog.btnCryo.isChecked = filterCryo
            bindingDialog.btnGeo.isChecked = filterGeo
            bindingDialog.btnSword.isChecked = filterSword
            bindingDialog.btnClaymore.isChecked = filterClaymore
            bindingDialog.btnPolearm.isChecked = filterPolearm
            bindingDialog.btnCatalyst.isChecked = filterCatalyst
            bindingDialog.btnBow.isChecked = filterBow

            dialog.show()
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
            dialog.window?.setGravity(Gravity.BOTTOM)
            dialog.setOnDismissListener {
                isFilterDialogOpen = false
            }
        }

    }

    //Reutilizacion para ToggleButtons
    private fun onToggleFilterChange(button: ToggleButton) : Boolean{
        val booleanFilter = if (button.isChecked) {
            button.alpha = 1.0f
            true
        } else {
            button.alpha = 0.5f
            false
        }
        return booleanFilter
    }

    //Aplicar filtroRareza
    private fun filtrarListaRareza(){
        val listaFiltrada = mutableListOf<CharactersResponse>()
        val texto  = if (Locale.getDefault().displayLanguage.lowercase() == "español") "No se ha encontrado ningun personaje" else "No character was found"

        if (filterRarity != 0) {
            for (character in characterList) {
                if (character.rarity == filterRarity) listaFiltrada.add(character)
            }
            characterList.clear()
            characterList.addAll(listaFiltrada)
            if (listaFiltrada.isEmpty()) binding.tvInformativoCharacters.text = texto
            else binding.tvInformativoCharacters.text = ""
        } else binding.tvInformativoCharacters.text = ""
    }

    //Aplicar filtroElementos
    private fun filtrarListaElementos(){
        val listaFiltrada = mutableListOf<CharactersResponse>()
        val texto  = if (Locale.getDefault().displayLanguage.lowercase() == "español") "No se ha encontrado ningun personaje" else "No character was found"

        for (character in characterList) {
            when (character.visionKey) {
                "PYRO" -> if (filterPyro) listaFiltrada.add(character)
                "HYDRO" -> if (filterHydro) listaFiltrada.add(character)
                "ANEMO" -> if (filterAnemo) listaFiltrada.add(character)
                "ELECTRO" -> if (filterElectro) listaFiltrada.add(character)
                "DENDRO" -> if (filterDendro) listaFiltrada.add(character)
                "CRYO" -> if (filterCryo) listaFiltrada.add(character)
                "GEO" -> if (filterGeo) listaFiltrada.add(character)
            }
        }
        characterList.clear()
        characterList.addAll(listaFiltrada)
        if (listaFiltrada.isEmpty()) binding.tvInformativoCharacters.text = texto
        else binding.tvInformativoCharacters.text = ""
    }

    //Aplicar filtroTipoArmas
    private fun filtrarListaTipoArma(){
        val listaFiltrada = mutableListOf<CharactersResponse>()
        val texto  = if (Locale.getDefault().displayLanguage.lowercase() == "español") "No se ha encontrado ningun personaje" else "No character was found"

        for (character in characterList) {
            when (character.weaponType) {
                "SWORD" -> if (filterSword) listaFiltrada.add(character)
                "CLAYMORE" -> if (filterClaymore) listaFiltrada.add(character)
                "POLEARM" -> if (filterPolearm) listaFiltrada.add(character)
                "CATALYST" -> if (filterCatalyst) listaFiltrada.add(character)
                "BOW" -> if (filterBow) listaFiltrada.add(character)
            }
        }
        characterList.clear()
        characterList.addAll(listaFiltrada)
        if (listaFiltrada.isEmpty()) binding.tvInformativoCharacters.text = texto
        else binding.tvInformativoCharacters.text = ""
    }

    //Adaptar a ancho de pantalla las columnas de el listado
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Calcular el número de columnas dinámicamente
        val screenWidth = resources.displayMetrics.widthPixels
        val desiredColumnWidthDp = 380 // Ancho deseado por columna en dp
        var spanCount = screenWidth / (desiredColumnWidthDp * resources.displayMetrics.density).toInt()
        if (spanCount < 1) spanCount = 1

        binding.rvCharacters.layoutManager = GridLayoutManager(context, spanCount)
    }

}

