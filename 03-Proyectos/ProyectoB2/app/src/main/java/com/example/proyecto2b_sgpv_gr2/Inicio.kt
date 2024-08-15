package com.example.proyecto2b_sgpv_gr2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto2b_sgpv_gr2.modelos.Genero
import com.example.proyecto2b_sgpv_gr2.modelos.Usuario
import com.example.proyecto2b_sgpv_gr2.modelos.Videojuego
import com.example.proyecto2b_sgpv_gr2.modelos.Resenia

import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase
import java.time.ZoneId

class Inicio : AppCompatActivity() {
    var arregloVideojuegos = arrayListOf<Videojuego>()
    var query: Query? = null
    lateinit var adaptador: RecyclerViewAdapterVideojuegos
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        var perfil = findViewById<LinearLayout>(R.id.ly_perfil)
        perfil.setOnClickListener{
            irActividad(Perfil::class.java)
        }

        var busqueda = findViewById<LinearLayout>(R.id.ly_busqueda)
        busqueda.setOnClickListener{
            irActividad(Busqueda::class.java)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.rv_videojuegos_catalogo)
        adaptador = RecyclerViewAdapterVideojuegos(
            this,
            arregloVideojuegos,
            recyclerView
        )
        //consultarColeccion()
        recyclerView.adapter = adaptador
        recyclerView.itemAnimator = androidx.recyclerview.widget
            .DefaultItemAnimator()
        recyclerView.layoutManager = androidx.recyclerview.widget
            .LinearLayoutManager(this)
        adaptador.notifyDataSetChanged()
    }

    fun irActividad(
        clase: Class<*>
    ){
        val intent = Intent(this, clase)
        startActivity(intent)
    }

    fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(
            findViewById(R.id.id_ly_inicio),
            texto, Snackbar.LENGTH_LONG
        )
        snack.show()
    }

    fun consultarColeccion() {
        val db = Firebase.firestore
        val ref = db.collection("videogame")
        var tarea: Task<QuerySnapshot>? = null
        tarea = ref.get() // 1era vez
        limpiarArreglo()
        adaptador.notifyDataSetChanged()
        if (tarea != null) {
            tarea
                .addOnSuccessListener { documentSnapshots ->
                    guardarQuery(documentSnapshots, ref)
                    for (videojuego in documentSnapshots) {
                        anadirAArreglo(videojuego)
                    }
                    adaptador.notifyDataSetChanged()
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
        document: QueryDocumentSnapshot
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

                arregloVideojuegos.add(videojuego)

                adaptador.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
            }
    }

    override fun onResume() {
        super.onResume()
        consultarColeccion()
        adaptador.notifyDataSetChanged()
    }

}