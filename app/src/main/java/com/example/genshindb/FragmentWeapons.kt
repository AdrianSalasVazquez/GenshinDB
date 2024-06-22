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
import com.example.genshindb.databinding.DialogFiltersWeaponsBinding
import com.example.genshindb.databinding.FragmentWeaponsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException
import java.util.Locale

class WeaponsFragment : Fragment() {

    private lateinit var binding: FragmentWeaponsBinding
    private lateinit var adapter: WeaponAdapter
    private val utiles: MainActivity = MainActivity()
    private val allWeaponsList = mutableListOf<WeaponsResponse>()
    private val weaponsList = mutableListOf<WeaponsResponse>()
    private val termination = ".json?print=pretty"
    private var isFilterDialogOpen = false

    //Variables filtros
    private var filterRarity = 0
    private var filterSword = true
    private var filterClaymore = true
    private var filterPolearm = true
    private var filterCatalyst = true
    private var filterBow = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentWeaponsBinding.inflate(layoutInflater)

        //Iniciar ReciclerView y busqueda inicial
        iniciarRecyclerView(weaponsList)
        buscarTodasLasArmas()

        //Buscador
        inicializarBuscador()

        //Filtros
        binding.btnFilterWeapons.setOnClickListener{
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
        fun newInstance(): WeaponsFragment = WeaponsFragment()
    }

    private fun iniciarRecyclerView(list : MutableList<WeaponsResponse>) {

        // Calcular el número de columnas dinámicamente
        val screenWidth = resources.displayMetrics.widthPixels
        val desiredColumnWidthDp = 180 // Ancho deseado por columna en dp
        var spanCount = screenWidth / (desiredColumnWidthDp * resources.displayMetrics.density).toInt()
        if (spanCount < 1) spanCount = 1

        //Añadir lista al adaptador, añadir el gridLayoutManager y añadirlo al recyclerview
        adapter = WeaponAdapter(list)
        binding.rvWeapons.layoutManager = GridLayoutManager(context, spanCount)
        binding.rvWeapons.adapter = adapter

        //OnItemClickListener
        adapter.setOnItemClickListener(object : WeaponAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                //Crear instancia de activity del arma y enviar el objeto arma
                val intent = Intent(context, WeaponDescActivity::class.java)
                intent.putExtra("weapon",list[position])
                startActivity(intent)
            }
        })
        //OnItemClickListener
    }

    private fun buscarTodasLasArmas() {
        CoroutineScope(Dispatchers.IO).launch {

            try {

                val call = utiles.getRetrofit().create(APIservice::class.java).getWeapons("weapons"+ termination)
                val weapons = call.body()
                activity?.runOnUiThread {
                    if (call.isSuccessful) {
                        val tryWeapons = weapons ?: emptyList()
                        allWeaponsList.addAll(tryWeapons)
                        weaponsList.addAll(allWeaponsList)
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
        binding.svWeapons.clearFocus()
        binding.svWeapons.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.tvInformativoWeapons.text = ""
                filtrarListaBusqueda(newText)
                return true
            }

        })
    }

    private fun filtrarListaBusqueda(query : String?){
        if (!query.isNullOrEmpty()) {
            val listaBuscada = mutableListOf<WeaponsResponse>()
            var texto = ""
            for (weapon in allWeaponsList) {
                texto = if (Locale.getDefault().displayLanguage.lowercase() == "español"){
                    if (weapon.spanish.name.lowercase().contains(query.lowercase())) listaBuscada.add(weapon)
                    "No se ha encontrado ningun arma"
                } else {
                    if (weapon.english.name.lowercase().contains(query.lowercase())) listaBuscada.add(weapon)
                    "No weapon was found"
                }
            }
            weaponsList.clear()
            weaponsList.addAll(listaBuscada)

            if (listaBuscada.isEmpty()) binding.tvInformativoWeapons.text = texto
        } else {
            weaponsList.clear()
            weaponsList.addAll(allWeaponsList)
        }
        filtrarListaRareza()
        filtrarListaTipoArma()
        adapter.notifyDataSetChanged()
    }

    private fun crearVentanaFiltros() {
        val bindingDialog = DialogFiltersWeaponsBinding.inflate(layoutInflater)
        val dialog = context?.let { Dialog(it) }
        if (dialog != null) {
            isFilterDialogOpen = true
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(bindingDialog.root)

            //RatingBar Rareza
            bindingDialog.ratingBarRarity.setOnRatingBarChangeListener { _, rating, _ ->
                filterRarity = rating.toInt()
                filtrarListaBusqueda(binding.svWeapons.query.toString())
            }

            //Onclick tipo de arma
            bindingDialog.btnSword.setOnCheckedChangeListener { _, _ ->
                filterSword = onToggleFilterChange(bindingDialog.btnSword)
                filtrarListaBusqueda(binding.svWeapons.query.toString())
            }
            bindingDialog.btnClaymore.setOnCheckedChangeListener { _, _ ->
                filterClaymore = onToggleFilterChange(bindingDialog.btnClaymore)
                filtrarListaBusqueda(binding.svWeapons.query.toString())
            }
            bindingDialog.btnPolearm.setOnCheckedChangeListener { _, _ ->
                filterPolearm = onToggleFilterChange(bindingDialog.btnPolearm)
                filtrarListaBusqueda(binding.svWeapons.query.toString())
            }
            bindingDialog.btnCatalyst.setOnCheckedChangeListener { _, _ ->
                filterCatalyst = onToggleFilterChange(bindingDialog.btnCatalyst)
                filtrarListaBusqueda(binding.svWeapons.query.toString())
            }
            bindingDialog.btnBow.setOnCheckedChangeListener { _, _ ->
                filterBow = onToggleFilterChange(bindingDialog.btnBow)
                filtrarListaBusqueda(binding.svWeapons.query.toString())
            }

            //Onclick Boton limpiar filtros
            bindingDialog.btnClearFilters.setOnClickListener{
                filterRarity = 0
                filterSword = true
                filterClaymore = true
                filterPolearm = true
                filterCatalyst = true
                filterBow = true
                bindingDialog.ratingBarRarity.rating = 0F
                bindingDialog.btnSword.isChecked = true
                bindingDialog.btnClaymore.isChecked = true
                bindingDialog.btnPolearm.isChecked = true
                bindingDialog.btnCatalyst.isChecked = true
                bindingDialog.btnBow.isChecked = true
                filtrarListaBusqueda(binding.svWeapons.query.toString())
            }

            //Inicializar Botones
            bindingDialog.ratingBarRarity.rating = filterRarity.toFloat()
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
        val listaFiltrada = mutableListOf<WeaponsResponse>()
        val texto  = if (Locale.getDefault().displayLanguage.lowercase() == "español") "No se ha encontrado ningun arma" else "No weapon was found"

        if (filterRarity != 0) {
            for (weapon in weaponsList) {
                if (weapon.rarity == filterRarity) listaFiltrada.add(weapon)
            }
            weaponsList.clear()
            weaponsList.addAll(listaFiltrada)
            if (listaFiltrada.isEmpty()) binding.tvInformativoWeapons.text = texto
            else binding.tvInformativoWeapons.text = ""
        } else binding.tvInformativoWeapons.text = ""
    }

    //Aplicar filtroTipoArmas
    private fun filtrarListaTipoArma(){
        val listaFiltrada = mutableListOf<WeaponsResponse>()
        val texto  = if (Locale.getDefault().displayLanguage.lowercase() == "español") "No se ha encontrado ningun arma" else "No weapon was found"

        for (weapon in weaponsList) {
            when (weapon.type.uppercase()) {
                "SWORD" -> if (filterSword) listaFiltrada.add(weapon)
                "CLAYMORE" -> if (filterClaymore) listaFiltrada.add(weapon)
                "POLEARM" -> if (filterPolearm) listaFiltrada.add(weapon)
                "CATALYST" -> if (filterCatalyst) listaFiltrada.add(weapon)
                "BOW" -> if (filterBow) listaFiltrada.add(weapon)
            }
        }
        weaponsList.clear()
        weaponsList.addAll(listaFiltrada)
        if (listaFiltrada.isEmpty()) binding.tvInformativoWeapons.text = texto
        else binding.tvInformativoWeapons.text = ""
    }

    //Adaptar a ancho de pantalla las columnas de el listado
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Calcular el número de columnas dinámicamente
        val screenWidth = resources.displayMetrics.widthPixels
        val desiredColumnWidthDp = 180 // Ancho deseado por columna en dp
        var spanCount = screenWidth / (desiredColumnWidthDp * resources.displayMetrics.density).toInt()
        if (spanCount < 1) spanCount = 1

        binding.rvWeapons.layoutManager = GridLayoutManager(context, spanCount)
    }

}