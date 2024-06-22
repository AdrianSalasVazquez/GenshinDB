package com.example.genshindb

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

//Adapter Personajes
class CharacterAdapter (private val characters:List<CharactersResponse>):RecyclerView.Adapter<CharacterViewHolder>() {

    // Inicio intento de clickListener
    private lateinit var cListener : OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener : OnItemClickListener){
        cListener = listener
    }
    // Fin intento de clickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.item_character, parent, false)
        return CharacterViewHolder(layoutInflater, cListener, parent.context)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val item = characters[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = characters.size

}

//Adapter Armas
class WeaponAdapter (private val weapons:List<WeaponsResponse>):RecyclerView.Adapter<WeaponViewHolder>() {

    // Inicio intento de clickListener
    private lateinit var cListener : OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener : OnItemClickListener){
        cListener = listener
    }
    // Fin intento de clickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeaponViewHolder{
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return WeaponViewHolder(layoutInflater, cListener)
    }

    override fun onBindViewHolder(holder: WeaponViewHolder, position: Int) {
        val item = weapons[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = weapons.size

}

//Adapter Artefactos
class ArtifactAdapter (private val artifact:List<ArtifactsResponse>):RecyclerView.Adapter<ArtifactViewHolder>() {

    // Inicio intento de clickListener
    private lateinit var cListener : OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener : OnItemClickListener){
        cListener = listener
    }
    // Fin intento de clickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtifactViewHolder{
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return ArtifactViewHolder(layoutInflater, cListener)
    }

    override fun onBindViewHolder(holder: ArtifactViewHolder, position: Int) {
        val item = artifact[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = artifact.size

}