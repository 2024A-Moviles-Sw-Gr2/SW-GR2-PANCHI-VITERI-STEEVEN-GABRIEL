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

class EditTiendaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_tienda)
        val idTienda=intent.getStringExtra("idTienda")

        if(idTienda !=null ){
            obtenerTiendaPorId(idTienda)
            val btnActualizar= findViewById<Button>(R.id.btn_actualizar_tienda)
            btnActualizar.setOnClickListener {
                actualizarTienda()
            }

            val btnBack=findViewById<Button>(R.id.btn_back_edit_tienda)
            btnBack.setOnClickListener {
                irActividad(MainActivity::class.java)
            }

        }else{
            mostrarSnackbar("No se ha encontrado el id de la tienda")
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

    fun obtenerTiendaPorId(idTienda: String){
        BDTienda.getTiendaById(idTienda){
            if( it != null){
                val id=findViewById<EditText>(R.id.et_id_tienda)
                val nombre=findViewById<EditText>(R.id.et_nombre_tienda)
                val telefono=findViewById<EditText>(R.id.et_telefono_tienda)
                val direccion=findViewById<EditText>(R.id.et_direccion_tienda)
                val estaAbierto=findViewById<CheckBox>(R.id.chk_abierto)

                id.setText(it.id)
                nombre.setText(it.nombre)
                telefono.setText(it.telefono)
                direccion.setText(it.direccion)
                estaAbierto.isChecked=it.estaAbierto

                mostrarSnackbar("Tienda encontrada")
            }else{
                mostrarSnackbar("Tienda no encontrada")

            }
        }

    }

    fun actualizarTienda(){
        val id=findViewById<EditText>(R.id.et_id_tienda).text.toString()
        val nombre=findViewById<EditText>(R.id.et_nombre_tienda).text.toString()
        val telefono=findViewById<EditText>(R.id.et_telefono_tienda).text.toString()
        val direccion=findViewById<EditText>(R.id.et_direccion_tienda).text.toString()
        val estaAbierto=findViewById<CheckBox>(R.id.chk_abierto).isChecked

        val tienda= Tienda(
            id,
            nombre,
            telefono,
            direccion,
            estaAbierto
        )

        BDTienda.updateTienda(tienda){

            if (it) {
                mostrarSnackbar("Tienda actualizada")
                irActividad(MainActivity::class.java)
            } else {
                mostrarSnackbar("Error al actualizar tienda")
            }

        }

    }

    fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }

}