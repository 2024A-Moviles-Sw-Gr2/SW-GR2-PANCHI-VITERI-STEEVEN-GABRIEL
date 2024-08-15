package com.example.examen2b_sgpv_gr2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import com.example.examen2b_sgpv_gr2.database.BDTienda
import com.example.examen2b_sgpv_gr2.models.Tienda
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
        val nombre=findViewById<EditText>(R.id.input_nombre_tienda)
        val telefono=findViewById<EditText>(R.id.input_telefono_tienda)
        val direccion=findViewById<EditText>(R.id.input_direccion_tienda)
        val estaAbierto=findViewById<CheckBox>(R.id.chk_abierto).isChecked

        BDTienda.createTienda(
            Tienda(
                "",
                nombre.text.toString(),
                telefono.text.toString(),
                direccion.text.toString(),
                estaAbierto
            )

        ){
            if(it){
                mostrarSnackbar("Se ha creado la tienda")
                irActividad(
                    MainActivity::class.java
                )
            } else{
                mostrarSnackbar("No se ha podido crear la tienda")
            }
        }


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