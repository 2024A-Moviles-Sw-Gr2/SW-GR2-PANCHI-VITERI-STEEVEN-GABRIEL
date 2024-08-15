package com.example.proyecto2b_sgpv_gr2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.proyecto2b_sgpv_gr2.modelos.Resenia
import com.example.proyecto2b_sgpv_gr2.modelos.Videojuego
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.storage.FirebaseStorage
import java.time.format.DateTimeFormatter

class DetalleVideojuego : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_videojuego)

        val videojuego: Videojuego? = intent.getSerializableExtra("videojuego") as? Videojuego

        if (videojuego != null) {
            findViewById<TextView>(R.id.tv_titulo_informacion).text = videojuego.titulo
            findViewById<TextView>(R.id.tv_calificacion_informacion).text = "${videojuego.calificacion}/5.0"
            findViewById<TextView>(R.id.tv_nombre_generos_videojuegos).text = videojuego.generos.joinToString("\n") { it?.nombre.orEmpty() }

            val storage = FirebaseStorage.getInstance()
            val storageReference = storage.reference
            val imageReference = storageReference.child(videojuego.imagen)

            imageReference.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                Glide.with(this)
                    .load(imageUrl)
                    .into(findViewById(R.id.im_videojuego_informacion))
            }.addOnFailureListener { exception ->
            }
            findViewById<TextView>(R.id.tv_nombre_plataforma).text = videojuego.plataforma

            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val fechaFormateada = videojuego.fecha.format(formatter)
            findViewById<TextView>(R.id.tv_fecha_lanzamiento).text = fechaFormateada
            findViewById<TextView>(R.id.tv_nombre_generos).text = videojuego.generos.joinToString("\n") { it?.nombre.orEmpty() }

            findViewById<TextView>(R.id.tv_nombre_editores).text = videojuego.elenco.joinToString("\n")
            findViewById<TextView>(R.id.tv_texto_descripcion).text = videojuego.resumen
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

        var resenias = findViewById<Button>(R.id.btn_reseñas)
        resenias.setOnClickListener{
            if (videojuego != null) {
                irActividadConVideojuego(ReseniaActivity::class.java, videojuego)
                finish()
            }
        }

        val toolbar = findViewById<MaterialToolbar>(R.id.mtb_informacion)
        toolbar.setNavigationOnClickListener {
            finish()
        }

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