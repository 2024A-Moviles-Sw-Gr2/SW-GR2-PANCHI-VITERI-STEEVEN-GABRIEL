package com.example.deber02

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.deber02.database.CrudProducto
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat

class CreateProductoActivity : AppCompatActivity() {

    var idTienda = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_producto)

        idTienda = intent.getStringExtra("idTienda").toString()
        mostrarSnackbar("$idTienda")

        val btnCrearProducto = findViewById<Button>(R.id.btn_crear_producto)
        btnCrearProducto.setOnClickListener {
            crearProducto()
        }

        val btnBack = findViewById<Button>(R.id.btn_back_create_producto)
        btnBack.setOnClickListener {
            //Crear un Intent para iniciar Ver Productos
            val intent = Intent(this, VerProductosActivity::class.java)

            //Pasar el parametro "nombre" al Intent
            intent.putExtra("id", idTienda)
            //Iniciar la actividad VerProductosActivity
            startActivity(intent)
        }
    }

    fun crearProducto() {
        val id = findViewById<EditText>(R.id.input_id_producto)
        val nombre = findViewById<EditText>(R.id.input_nombre_producto)
        val precio = findViewById<EditText>(R.id.input_precio_producto)
        val fechaCaducidad = findViewById<EditText>(R.id.input_fecha_producto)
        val idtienda = Integer.parseInt(idTienda)

        CrudProducto().crearProducto(
            id.text.toString().toInt(),
            nombre.text.toString(),
            precio.text.toString().toDouble(),
            SimpleDateFormat("dd/MM/yyyy").parse(fechaCaducidad.text.toString()),
            idtienda
        )

        mostrarSnackbar("Se ha creado la canción ${nombre.text} y el id genero es $idtienda")


        //Crear un intent para iniciar Ver Productos
        val intent = Intent(this, VerProductosActivity::class.java)

        //Pasar el parámetro "nombre" al intent
        intent.putExtra("id", idTienda)
        startActivity(intent)
    }

    fun mostrarSnackbar(texto:String){
        Snackbar
            .make(
                findViewById(R.id.form_create_producto),
                texto,
                Snackbar.LENGTH_LONG//tiempo
            )
            .show()
    }



}