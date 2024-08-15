package com.example.proyecto2b_sgpv_gr2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyecto2b_sgpv_gr2.modelos.Videojuego
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage

class Resenia : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resenia)

        val videojuego: Videojuego? = intent.getSerializableExtra("videojuego") as? Videojuego

        if (videojuego != null) {
            findViewById<TextView>(R.id.tv_titulo_informacion).text = videojuego.titulo
            findViewById<TextView>(R.id.tv_calificacion_informacion).text = "${videojuego.calificacion}/5.0"
            findViewById<TextView>(R.id.tv_nombre_generos).text =  videojuego.generos.joinToString("\n") { it?.nombre.orEmpty() }

            val storage = FirebaseStorage.getInstance()
            val storageReference = storage.reference
            val imageReference = storageReference.child(videojuego.imagen)

            imageReference.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                Glide.with(this)
                    .load(imageUrl)
                    .into(findViewById(R.id.iv_videojuego_reseñas))
            }.addOnFailureListener { exception ->
            }
        }

        var perfil = findViewById<LinearLayout>(R.id.ly_perfil)
        perfil.setOnClickListener{
            irActividad(Perfil::class.java)
            finish()
        }

        var busqueda = findViewById<LinearLayout>(R.id.ly_busqueda)
        busqueda.setOnClickListener{
            irActividad(Busqueda::class.java)
            finish()
        }

        var inicio = findViewById<LinearLayout>(R.id.ly_inicio)
        inicio.setOnClickListener{
            irActividad(Inicio::class.java)
            finish()
        }


        val toolbar = findViewById<MaterialToolbar>(R.id.mtb_reseña)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.rv_reseñas)
        val adaptador = RecyclerViewAdapterResenias(
            this,
            videojuego!!.resenias,
            recyclerView
        )
        recyclerView.adapter = adaptador
        recyclerView.itemAnimator = androidx.recyclerview.widget
            .DefaultItemAnimator()
        recyclerView.layoutManager = androidx.recyclerview.widget
            .LinearLayoutManager(this)
        adaptador.notifyDataSetChanged()

        var mAuth = FirebaseAuth.getInstance()


    }

    fun irActividad(
        clase: Class<*>
    ){
        val intent = Intent(this, clase)
        startActivity(intent)
    }

    fun irActividadConVideojuego(
        clase: Class<*>,
        videojuego: Videojuego
    ){
        val intent = Intent(this, clase)
        intent.putExtra("videojuego", videojuego)
        startActivity(intent)
    }
}