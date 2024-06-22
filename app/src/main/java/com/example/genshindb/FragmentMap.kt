package com.example.genshindb

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebViewClient
import com.example.genshindb.databinding.FragmentMapBinding

class FragmentMap : Fragment() {

    private lateinit var binding: FragmentMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMapBinding.inflate(layoutInflater)

        //Añadir ajustes web al webview
        val webSettings: WebSettings = binding.webviewMap.settings
        // Habilitar JavaScript
        webSettings.javaScriptEnabled = true
        // Habilitar almacenamiento local
        webSettings.domStorageEnabled = true
        // Habilitar carga de imágenes automáticamente
        webSettings.loadsImagesAutomatically = true
        // Añade un cliente web para la carga adecuada en el webview
        binding.webviewMap.webViewClient = WebViewClient()
        // Cargar la url del mapa interactivo a el webview
        binding.webviewMap.loadUrl("https://act.hoyolab.com/ys/app/interactive-map/index.html")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        fun newInstance() : FragmentMap = FragmentMap()
    }
}