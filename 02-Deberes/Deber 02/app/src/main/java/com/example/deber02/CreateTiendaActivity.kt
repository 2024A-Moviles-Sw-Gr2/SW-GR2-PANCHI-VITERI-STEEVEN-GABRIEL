package com.example.deber02

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import com.example.deber02.database.CrudTienda
import com.google.android.material.snackbar.Snackbar

class CreateTiendaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_tienda)

        val btnCrearTienda=findViewById<Button>(R.id.btn_crear_tienda)

        btnCrearTienda.setOnClickListener {
            crearTienda()
            irActividad(MainActivity::class.java)
        }

        val btnBackP=findViewById<Button>(R.id.btn_back_create_tienda)
        btnBackP.setOnClickListener{
            irActividad(MainActivity::class.java)
        }
    }

    fun crearTienda(){
        //Obtener componentes
        val id=findViewById<EditText>(R.id.input_id_tienda)
        val nombre=findViewById<EditText>(R.id.input_nombre_tienda)
        val telefono=findViewById<EditText>(R.id.input_telefono_tienda)
        val direccion=findViewById<EditText>(R.id.input_direccion_tienda)
        val estaAbierto=findViewById<CheckBox>(R.id.chk_abierto).isChecked

        CrudTienda().crearTienda(
            id.text.toString().toInt(),
            nombre.text.toString(),
            telefono.text.toString(),
            direccion.text.toString(),
            estaAbierto,
        )
        mostrarSnackbar("Se ha creado la tienda ${nombre.text}")
        irActividad(
            MainActivity::class.java
        )

    }

    fun mostrarSnackbar(texto: String){
        Snackbar.make(
            findViewById(R.id.form_create_tienda),
            texto,
            Snackbar.LENGTH_LONG
        ).show()
    }

    fun irActividad(
        clase:Class<*>
    ){
        val intent= Intent(this, clase)
        startActivity(intent)
    }
}