package com.example.examen2b_sgpv_gr2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.examen2b_sgpv_gr2.database.BDProducto
import com.example.examen2b_sgpv_gr2.models.Producto
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat

class CreateProductoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_producto)

        val idTienda = intent.getStringExtra("idTienda")

        if(idTienda != null){
            val btnCrearProducto = findViewById<Button>(R.id.btn_crear_producto)
            btnCrearProducto.setOnClickListener {
                crearProducto(idTienda)
            }
        }else{
            mostrarSnackbar("No se ha seleccionado una tienda")
        }

    }

    fun crearProducto(idTienda: String) {
        val nombre = findViewById<EditText>(R.id.input_nombre_producto)
        val precio = findViewById<EditText>(R.id.input_precio_producto)
        val fechaCaducidad = findViewById<EditText>(R.id.input_fecha_producto)

        BDProducto.createProducto(
            Producto(
                "",
                nombre.text.toString(),
                precio.text.toString().toDouble(),
                SimpleDateFormat("dd/MM/yyyy").parse(fechaCaducidad.text.toString()),
                idTienda
            )
        ){
            if (it){
                mostrarSnackbar("Se ha creado el producto")
                val intent = Intent(this, VerProductosActivity::class.java)
                intent.putExtra("idTienda", idTienda)
                startActivity(intent)
            }else{
                mostrarSnackbar("No se ha podido crear el producto")
            }
        }
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