package com.example.genshindb

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.genshindb.databinding.FragmentCharBuildBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentCharBuild : Fragment() {

    private lateinit var binding: FragmentCharBuildBinding
    private lateinit var character: CharactersResponse
    private lateinit var artifactAdapter: ArtifactAdapter
    private lateinit var weaponAdapter: WeaponAdapter
    private val utiles: MainActivity = MainActivity()
    private val artifactList = mutableListOf<ArtifactsResponse>()
    private val weaponsList = mutableListOf<WeaponsResponse>()
    private val termination = ".json?print=pretty"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCharBuildBinding.inflate(layoutInflater)
        arguments?.let {
            character = it.getSerializable(ARG_PERSONAJE) as CharactersResponse
        }

        //Construccion del interfaz
        construccionInterfazConstruccion()

        //Inicializar los reciclerviews y sacar las armas y artefactos que le interesan al personaje
        iniciarRecyclerViewWeaponsRec(weaponsList)
        iniciarRecyclerViewArtefactosRec(artifactList)
        getArmasYArtefactosRecomendados()

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
        fun newInstance(personaje: CharactersResponse): FragmentCharBuild {
            val fragment = FragmentCharBuild()
            val args = Bundle()
            args.putSerializable(ARG_PERSONAJE, personaje)
            fragment.arguments = args
            return fragment
        }
    }

    private fun construccionInterfazConstruccion() {

        //Dependiendo del elemento del personaje
        val colorElementoDark : Int
        val backgroundElemento : Int
        when (character.visionKey) {
            "ANEMO" -> {
                colorElementoDark = R.color.anemo_dark
                backgroundElemento = R.drawable.background_anemo
            }
            "CRYO" -> {
                colorElementoDark = R.color.cryo_dark
                backgroundElemento = R.drawable.background_cryo
            }
            "DENDRO" -> {
                colorElementoDark = R.color.dendro_dark
                backgroundElemento = R.drawable.background_dendro
            }
            "ELECTRO" -> {
                colorElementoDark = R.color.electro_dark
                backgroundElemento = R.drawable.background_electro
            }
            "GEO" -> {
                colorElementoDark = R.color.geo_dark
                backgroundElemento = R.drawable.background_geo
            }
            "HYDRO" -> {
                colorElementoDark = R.color.hydro_dark
                backgroundElemento = R.drawable.background_hydro
            }
            "PYRO" -> {
                colorElementoDark = R.color.pyro_dark
                backgroundElemento = R.drawable.background_pyro
            }
            else -> {
                colorElementoDark = 0
                backgroundElemento = 0
            }
        }
        //Recuperando los recursos
        val colorElementoDarkRecurso = context?.let { ContextCompat.getColor(it, colorElementoDark) }
        val backgroundElementoRecurso = context?.let { ContextCompat.getDrawable(it,backgroundElemento) }

        // Cambio del background segun elemento
        binding.scrollConstruccion.background = backgroundElementoRecurso

        // Cambiar titulos a color del elemento
        if (colorElementoDarkRecurso != null) {
            binding.titleRecWeapons.setTextColor(colorElementoDarkRecurso)
            binding.titleRecArtifacts.setTextColor(colorElementoDarkRecurso)
        }



    }

    // Conectar el adaptador con el recyclerview de las armas
    private fun iniciarRecyclerViewWeaponsRec(list : MutableList<WeaponsResponse>) {
        // Calcular el número de columnas dinámicamente
        val screenWidth = resources.displayMetrics.widthPixels
        val desiredColumnWidthDp = 200 // Ancho deseado por columna en dp
        var spanCount = screenWidth / (desiredColumnWidthDp * resources.displayMetrics.density).toInt()
        if (spanCount < 1) spanCount = 1


        //Añadir lista al adaptador, añadir el gridLayoutManager y añadirlo al recyclerview
        weaponAdapter = WeaponAdapter(list)
        binding.recyclerRecWeapons.layoutManager = GridLayoutManager(context, spanCount)
        binding.recyclerRecWeapons.adapter = weaponAdapter

        //OnItemClickListener
        weaponAdapter.setOnItemClickListener(object : WeaponAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                //Crear instancia de activity del arma y enviar el objeto arma
                val intent = Intent(context, WeaponDescActivity::class.java)
                intent.putExtra("weapon",list[position])
                startActivity(intent)
            }
        })
        //OnItemClickListener
    }
    // Conectar el adaptador con el recyclerview de los artefactos
    private fun iniciarRecyclerViewArtefactosRec(list : MutableList<ArtifactsResponse>) {
        // Calcular el número de columnas dinámicamente
        val screenWidth = resources.displayMetrics.widthPixels
        val desiredColumnWidthDp = 200 // Ancho deseado por columna en dp
        var spanCount = screenWidth / (desiredColumnWidthDp * resources.displayMetrics.density).toInt()
        if (spanCount < 1) spanCount = 1

        //Añadir lista al adaptador, añadir el gridLayoutManager y añadirlo al recyclerview
        artifactAdapter = ArtifactAdapter(list)
        binding.recyclerRecArtifacts.layoutManager = GridLayoutManager(context, spanCount)
        binding.recyclerRecArtifacts.adapter = artifactAdapter

        //OnItemClickListener
        artifactAdapter.setOnItemClickListener(object : ArtifactAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                //Crear instancia de activity del artefacto y enviar el objeto artefacto
                val intent = Intent(context, ArtifactDescActivity::class.java)
                intent.putExtra("artifact",list[position])
                startActivity(intent)
            }
        })
        //OnItemClickListener
    }

    private fun getArmasYArtefactosRecomendados() {
        //Corrutina Armas
        CoroutineScope(Dispatchers.IO).launch {
            val call = utiles.getRetrofit().create(APIservice::class.java).getWeapons("weapons"+ termination)
            val weapons = call.body()
            activity?.runOnUiThread {
                if (call.isSuccessful) {
                    val armasFiltradas = mutableListOf<WeaponsResponse>()
                    if (weapons != null) {
                        for (weaponName in character.build.recommended_weapons){
                            for (weapon in weapons){
                                if (weapon.english.name.lowercase() == weaponName.lowercase()){
                                    armasFiltradas.add(weapon)
                                    break
                                }
                            }
                        }
                    }
                    weaponsList.clear()
                    weaponsList.addAll(armasFiltradas)
                    weaponAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(context, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
                }
            }
        }
        //Corrutina Artefactos
        CoroutineScope(Dispatchers.IO).launch {
            val call = utiles.getRetrofit().create(APIservice::class.java).getArtifacts("artifacts"+ termination)
            val artifacts = call.body()
            activity?.runOnUiThread {
                if (call.isSuccessful) {
                    val artefactosFiltrados = mutableListOf<ArtifactsResponse>()
                    if (artifacts != null) {
                        for (artifactName in character.build.recommended_artifacts){
                            for (artifact in artifacts){
                                if (artifact.english.name.lowercase() == artifactName.lowercase()){
                                    artefactosFiltrados.add(artifact)
                                    break
                                }
                            }
                        }
                    }
                    artifactList.clear()
                    artifactList.addAll(artefactosFiltrados)
                    artifactAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(context, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}