package com.example.a2024aswgr2sgpv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import com.example.a2024aswgr2sgpv.database.CrudTienda
import com.example.a2024aswgr2sgpv.database.DBMemoria
import com.example.a2024aswgr2sgpv.models.Tienda
import com.example.myapplication.R
import com.google.android.material.snackbar.Snackbar
import java.lang.Integer.parseInt

class EditTiendaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_tienda)
        val idTienda=intent.getStringExtra("id")

        val tienda=
            DBMemoria.arregloTienda.find{tienda -> tienda.id==parseInt(idTienda)}
        llenarInputs(tienda!!)
        mostrarSnackbar("$tienda")

        val btnActualizar= findViewById<Button>(R.id.btn_actualizar_tienda)
        btnActualizar.setOnClickListener {
            actualizarTienda()
            mostrarSnackbar("Tienda actualizada")
            irActividad(MainActivity::class.java)

        }

        val btnBack=findViewById<Button>(R.id.btn_back_edit_tienda)
        btnBack.setOnClickListener {
            irActividad(MainActivity::class.java)
        }
    }

    fun mostrarSnackbar(texto: String){
        Snackbar
            .make(
                findViewById(R.id.form_edit_tienda),
                texto,
                Snackbar.LENGTH_LONG
            )
            .show()
    }

    fun llenarInputs(tienda: Tienda){
        //Obtener componentes
        val id=findViewById<EditText>(R.id.et_id_tienda)
        val nombre=findViewById<EditText>(R.id.et_nombre_tienda)
        val telefono=findViewById<EditText>(R.id.et_telefono_tienda)
        val direccion=findViewById<EditText>(R.id.et_direccion_tienda)
        val estaAbierto=findViewById<CheckBox>(R.id.chk_abierto)

        id.setText(tienda.id.toString())
        nombre.setText(tienda.nombre)
        telefono.setText(tienda.telefono)
        direccion.setText(tienda.direccion.toString())
        estaAbierto.isChecked=tienda.estaAbierto

    }

    fun actualizarTienda(){
        val id=findViewById<EditText>(R.id.et_id_tienda)
        val nombre=findViewById<EditText>(R.id.et_nombre_tienda)
        val telefono=findViewById<EditText>(R.id.et_telefono_tienda)
        val direccion=findViewById<EditText>(R.id.et_direccion_tienda)
        val estaAbierto=findViewById<CheckBox>(R.id.chk_abierto)

        CrudTienda().editarTienda(
            id.text.toString().toInt(),
            nombre.text.toString(),
            telefono.text.toString(),
            direccion.text.toString(),
            estaAbierto.isChecked
        )
    }
    fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }

}