package com.example.genshindb

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.genshindb.databinding.FragmentHomePageBinding

class FragmentHomePage : Fragment() {

    private lateinit var binding: FragmentHomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomePageBinding.inflate(layoutInflater)

        //Boton registro diario
        binding.buttonDailyLogin.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("http://webstatic-sea.mihoyo.com/ys/event/signin-sea/index.html?act_id=e202102251931481")
            startActivity(intent)
        }

        //Boton calculadora de mejoras
        binding.buttonProgressionCalculator.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://webstatic-sea.hoyolab.com/ys/event/calculator-sea/index.html?bbs_presentation_style=fullscreen&bbs_auth_required=true&utm_source=share&utm_medium=hoyolab&utm_campaign=app")
            startActivity(intent)
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
        fun newInstance(): FragmentHomePage = FragmentHomePage()
    }
}