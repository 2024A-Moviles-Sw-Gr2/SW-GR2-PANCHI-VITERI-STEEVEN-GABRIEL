package com.example.proyecto2b_sgpv_gr2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto2b_sgpv_gr2.modelos.Genero
import com.example.proyecto2b_sgpv_gr2.modelos.Resenia
import com.example.proyecto2b_sgpv_gr2.modelos.Usuario
import com.example.proyecto2b_sgpv_gr2.modelos.Videojuego

import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.ZoneId
import java.util.Locale

class Busqueda : AppCompatActivity() {
    var arregloVideojuegos = arrayListOf<Videojuego>()
    var query: Query? = null
    lateinit var adaptadorVideojuegos: RecyclerViewAdapterVideojuegos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_busqueda)

        var perfil = findViewById<LinearLayout>(R.id.ly_perfil)
        perfil.setOnClickListener{
            irActividad(Perfil::class.java)
            finish()
        }

        var inicio = findViewById<LinearLayout>(R.id.ly_inicio)
        inicio.setOnClickListener{
            irActividad(Inicio::class.java)
            finish()
        }

        var arregloGeneros = arrayListOf<Genero>()
        arregloGeneros.add(Genero.ACCION)
        arregloGeneros.add(Genero.AVENTURAS)
        arregloGeneros.add(Genero.CARRERAS)
        arregloGeneros.add(Genero.DEPORTES)
        arregloGeneros.add(Genero.ESTRATEGIA)

        val recyclerView = findViewById<RecyclerView>(R.id.rv_generos)
        val adaptadorGenero = RecyclerViewAdapterGeneros(
            this,
            arregloGeneros,
            recyclerView
        )
        recyclerView.adapter = adaptadorGenero
        recyclerView.itemAnimator = androidx.recyclerview.widget
            .DefaultItemAnimator()
        recyclerView.layoutManager = androidx.recyclerview.widget
            .LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adaptadorGenero.notifyDataSetChanged()

        val recyclerViewVideojuego = findViewById<RecyclerView>(R.id.rv_videojuegos)
        adaptadorVideojuegos = RecyclerViewAdapterVideojuegos(
            this,
            arregloVideojuegos,
            recyclerViewVideojuego
        )
        //consultarColeccion()
        recyclerViewVideojuego.adapter = adaptadorVideojuegos
        recyclerViewVideojuego.itemAnimator = androidx.recyclerview.widget
            .DefaultItemAnimator()
        recyclerViewVideojuego.layoutManager = androidx.recyclerview.widget
            .LinearLayoutManager(this)
        adaptadorVideojuegos.notifyDataSetChanged()

        val ptIngresarVideojuego = findViewById<EditText>(R.id.pt_ingresar_videojuego)
        ptIngresarVideojuego.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Llama a la función de búsqueda con el texto ingresado
                consultarColeccion(null,s.toString())
                adaptadorVideojuegos.notifyDataSetChanged()
            }
        })
    }

    fun irActividad(
        clase: Class<*>
    ){
        val intent = Intent(this, clase)
        startActivity(intent)
    }

    fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(
            findViewById(R.id.id_ly_busqueda),
            texto, Snackbar.LENGTH_LONG
        )
        snack.show()
    }

    fun consultarColeccion(generoFiltrado: Genero? = null, titulo: String="") {
        val db = Firebase.firestore
        val ref = db.collection("videogame")
        var tarea: Task<QuerySnapshot>? = null
        tarea = ref.get() // 1era vez
        limpiarArreglo()
        adaptadorVideojuegos.notifyDataSetChanged()
        if (tarea != null) {
            tarea
                .addOnSuccessListener { documentSnapshots ->
                    guardarQuery(documentSnapshots, ref)
                    for (videojuego in documentSnapshots) {
                        anadirAArreglo(videojuego, generoFiltrado, titulo)
                    }
                    adaptadorVideojuegos.notifyDataSetChanged()
                }
                .addOnFailureListener { }
        }
    }

    fun limpiarArreglo() {
        arregloVideojuegos.clear()
    }

    fun guardarQuery(
        documentSnapshots: QuerySnapshot,
        ref: Query
    ) {
        if (documentSnapshots.size() > 0) {
            val ultimoDocumento = documentSnapshots
                .documents[documentSnapshots.size() - 1]
            query = ref
                // Start After nos ayuda a paginar
                .startAfter(ultimoDocumento)
        }
    }

    fun anadirAArreglo(
        document: QueryDocumentSnapshot,
        generoFiltrado: Genero?,
        tituloBuscado: String
    ) {
        val videojuego = Videojuego(
            document.id,
            document.data.get("title") as String,
            (document.data.get("score") as? Number)?.toDouble() ?: 0.0,
            (document.data.get("genres") as ArrayList<Long>).map { Genero.obtenerPorId(it.toInt()) }.toTypedArray(),
            document.data.get("image") as String,
            document.data.get("platform") as String,
            (document.data.get("date") as Timestamp).toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
            ArrayList(),
            (document.data.get("cast") as ArrayList<String>).toTypedArray(),
            document.data.get("resumen") as String,
        )
        val reviewCollection = document.reference.collection("review")

        reviewCollection.get()
            .addOnSuccessListener { reviewsSnapshot ->
                val listaResenias = reviewsSnapshot.documents.map { resenia ->
                    Resenia(
                        Usuario(resenia.get("user") as String),
                        (resenia.get("date") as Timestamp).toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        resenia.get("comment") as String,
                        (resenia.get("score") as? Number)?.toDouble() ?: 0.0
                    )
                }
                videojuego.resenias = ArrayList(listaResenias)

                if (generoFiltrado == null || videojuego.generos.any { it == generoFiltrado }) {
                    if (tituloBuscado.isEmpty() || videojuego.titulo.lowercase(Locale.getDefault()).contains(tituloBuscado.lowercase(Locale.getDefault()))) {
                        arregloVideojuegos.add(videojuego)
                    }
                }

                adaptadorVideojuegos.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                // Manejar errores en la consulta de reseñas si es necesario
            }
    }



}