package com.example.genshindb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.genshindb.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val KEY_SELECTED_MENU_ID = "selected_menu_id"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentFragment: Fragment? = null
    private var selectedMenuItemId = R.id.MenuInicio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Fuerza la aplicacion a estar en modo oscuro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)


        // Recuperar el estado anterior si está disponible
        savedInstanceState?.let {
            selectedMenuItemId = it.getInt(KEY_SELECTED_MENU_ID)
        }

        // Mostrar el fragmento actual
        mostrarFragmento(getFragmentoPorItemMenu(selectedMenuItemId))

        // Seleccionar el ítem de menú previamente seleccionado
        binding.bottomNavigation.selectedItemId = selectedMenuItemId

        //Iniciamos el funcionamiento del menu inferior
        inicializarMenu(binding.bottomNavigation)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Guardar el estado del fragmento actual si existe
        outState.putInt(KEY_SELECTED_MENU_ID, selectedMenuItemId)
    }


    // Inicializa el menu inferior
    private fun inicializarMenu(menu: BottomNavigationView) {
        menu.setOnItemSelectedListener { item ->
            if (item.itemId != selectedMenuItemId){
                selectedMenuItemId = item.itemId
                mostrarFragmento(getFragmentoPorItemMenu(selectedMenuItemId))
            }
            true
        }
    }

    //En funcion al item de menu seleccionado te devuelve el fragmento correspondiente
    private fun getFragmentoPorItemMenu(menuItemId: Int): Fragment {
        return when (menuItemId) {
            R.id.MenuCharacters -> CharactersFragment.newInstance()
            R.id.MenuWeapons -> WeaponsFragment.newInstance()
            R.id.MenuInicio -> FragmentHomePage.newInstance()
            R.id.MenuArtifacts -> FragmentArtifacts.newInstance()
            R.id.MenuMap -> FragmentMap.newInstance()
            else -> FragmentHomePage.newInstance() // Fragmento por defecto
        }
    }

    //Hace la transaccion del nuevo fragmento
    private fun mostrarFragmento(fragment: Fragment) {
        if (fragment != currentFragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.contenedor, fragment)
                .commit()
            currentFragment = fragment
        }
    }

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://genshindb-a57e6-default-rtdb.europe-west1.firebasedatabase.app/types/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}
