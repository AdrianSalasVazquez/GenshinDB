package com.example.genshindb

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.genshindb.databinding.FragmentArtifactsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException
import java.util.*

class FragmentArtifacts : Fragment() {

    private lateinit var binding: FragmentArtifactsBinding
    private lateinit var adapter: ArtifactAdapter
    private val utiles: MainActivity = MainActivity()
    private val ArtifactList = mutableListOf<ArtifactsResponse>()
    private val termination = ".json?print=pretty"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentArtifactsBinding.inflate(layoutInflater)

        //Iniciar ReciclerView y busqueda inicial
        iniciarRecyclerView(ArtifactList)
        buscarTodosLosArtefactos()

        //Buscador
        inicializarBuscador()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        fun newInstance(): FragmentArtifacts = FragmentArtifacts()
    }


    private fun iniciarRecyclerView(list : MutableList<ArtifactsResponse>) {

        // Calcular el número de columnas dinámicamente
        val screenWidth = resources.displayMetrics.widthPixels
        val desiredColumnWidthDp = 180 // Ancho deseado por columna en dp
        var spanCount = screenWidth / (desiredColumnWidthDp * resources.displayMetrics.density).toInt()
        if (spanCount < 1) spanCount = 1

        //Añadir lista al adaptador, añadir el gridLayoutManager y añadirlo al recyclerview
        adapter = ArtifactAdapter(list)
        binding.rvArtifacts.layoutManager = GridLayoutManager(context, spanCount)
        binding.rvArtifacts.adapter = adapter

        //OnItemClickListener
        adapter.setOnItemClickListener(object : ArtifactAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                //Crear instancia de activity del artefacto y enviar el objeto artefacto
                val intent = Intent(context, ArtifactDescActivity::class.java)
                intent.putExtra("artifact",list[position])
                startActivity(intent)
            }
        })
        //OnItemClickListener
    }

    private fun buscarTodosLosArtefactos() {
        CoroutineScope(Dispatchers.IO).launch {

            try {

                val call = utiles.getRetrofit().create(APIservice::class.java).getArtifacts("artifacts"+ termination)
                val artifacts = call.body()
                activity?.runOnUiThread {
                    if (call.isSuccessful) {
                        val tryArtifacts = artifacts ?: emptyList()
                        ArtifactList.clear()
                        ArtifactList.addAll(tryArtifacts)
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
        binding.svArtifacts.clearFocus()
        binding.svArtifacts.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.tvInformativoArtifacts.text = ""
                filtrarLista(newText)
                return true
            }

        })
    }

    private fun filtrarLista(query : String?){
        if (query != null) {
            val listaFiltrada = mutableListOf<ArtifactsResponse>()
            var texto = ""
            for (artifact in ArtifactList) {
                texto = if (Locale.getDefault().displayLanguage.lowercase() == "español"){
                    if (artifact.spanish.name.lowercase().contains(query.lowercase())) listaFiltrada.add(artifact)
                    "No se ha encontrado ningun artefacto"
                } else {
                    if (artifact.english.name.lowercase().contains(query.lowercase())) listaFiltrada.add(artifact)
                    "No artifact was found"
                }
            }
            iniciarRecyclerView(listaFiltrada)
            if (listaFiltrada.isEmpty()) binding.tvInformativoArtifacts.text = texto
        }
    }

    //Adaptar a ancho de pantalla las columnas de el listado
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Calcular el número de columnas dinámicamente
        val screenWidth = resources.displayMetrics.widthPixels
        val desiredColumnWidthDp = 180 // Ancho deseado por columna en dp
        var spanCount = screenWidth / (desiredColumnWidthDp * resources.displayMetrics.density).toInt()
        if (spanCount < 1) spanCount = 1

        binding.rvArtifacts.layoutManager = GridLayoutManager(context, spanCount)
    }
}