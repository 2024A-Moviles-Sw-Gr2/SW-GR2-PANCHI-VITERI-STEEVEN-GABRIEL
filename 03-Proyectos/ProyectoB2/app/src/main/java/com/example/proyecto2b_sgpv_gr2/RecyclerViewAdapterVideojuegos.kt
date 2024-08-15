package com.example.proyecto2b_sgpv_gr2

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto2b_sgpv_gr2.modelos.Videojuego

import com.google.firebase.storage.FirebaseStorage
import java.util.ArrayList

class RecyclerViewAdapterVideojuegos (
    private val contexto: Context,
    private val lista: ArrayList<Videojuego>,
    private val recyclearView: RecyclerView
): RecyclerView.Adapter <RecyclerViewAdapterVideojuegos.MyViewHolder>(){
    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val imageView: ImageView
        val tituloTextView: TextView
        val calificacionTextView: TextView
        val generoTextView: TextView
        val informacionButton: ImageButton

        init {
            imageView = view.findViewById(R.id.iv_videojuegos)
            tituloTextView = view.findViewById(R.id.tv_titulo)
            calificacionTextView = view.findViewById(R.id.tv_calificacion)
            generoTextView = view.findViewById(R.id.tv_nombre_generos)
            informacionButton = view.findViewById(R.id.ib_informacion)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.recycler_view_videojuego,
                parent,
                false
            )
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return this.lista.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val videojuegoActual = this.lista[position]
        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.reference

        val imageReference = storageReference.child(videojuegoActual.imagen)

        imageReference.downloadUrl.addOnSuccessListener { uri ->
            Log.d("FirebaseStorage", "Image URL: $uri")
            val imageUrl = uri.toString()
            Glide.with(contexto)
                .load(imageUrl)
                .into(holder.imageView)
        }.addOnFailureListener { exception ->
            Toast.makeText(contexto, "Error al cargar la imagen: ${exception.message}", Toast.LENGTH_SHORT).show()
        }


        holder.tituloTextView.text = videojuegoActual.titulo
        holder.calificacionTextView.text = "${videojuegoActual.calificacion}/5.0"
        holder.generoTextView.text = videojuegoActual.generos.joinToString("\n") { it?.nombre.orEmpty() }


        holder.informacionButton.setOnClickListener{
            irActividad(DetalleVideojuego::class.java, videojuegoActual)
        }
    }

    fun irActividad(
        clase: Class<*>,
        videojuego: Videojuego
    ){
        val intent = Intent(contexto, clase)
        intent.putExtra("videojuego", videojuego)
        contexto.startActivity(intent)
    }

}