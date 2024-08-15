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

class EditProductoActivity : AppCompatActivity() {

    var idTienda=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_producto)

        val idProducto=intent.getStringExtra("idProducto")

        if (idProducto!= null){
            getProductoById(idProducto)
            val btnActualizar= findViewById<Button>(R.id.btn_actualizar_producto)
            btnActualizar.setOnClickListener {
                actualizarProducto(idTienda)
            }
        }else{
            mostrarSnackbar("No se ha encontrado el id del producto")
            irActividad(MainActivity::class.java)
        }

    }


    fun getProductoById(idProducto: String){
        BDProducto.getProductoById(idProducto){
            if(it != null){
                val id=findViewById<EditText>(R.id.et_id_producto)
                val nombre=findViewById<EditText>(R.id.et_nombre_producto)
                val precio=findViewById<EditText>(R.id.et_precio_producto)
                val fechaCaducidad= findViewById<EditText>(R.id.et_fecha_producto)

                idTienda=it.idTienda
                id.setText(idProducto)
                nombre.setText(it.nombre)
                precio.setText(it.precio.toString())
                fechaCaducidad.setText(SimpleDateFormat("dd/MM/yyyy").format(it.fechaCaducidad))

                mostrarSnackbar("Producto encontrado")

            }else{
                mostrarSnackbar("Producto no encontrado")
            }
        }
    }

    fun actualizarProducto(idTienda:String){

        val id=findViewById<EditText>(R.id.et_id_producto).text.toString()
        val nombre=findViewById<EditText>(R.id.et_nombre_producto).text.toString()
        val precio=findViewById<EditText>(R.id.et_precio_producto).text.toString().toDouble()
        val fechaCaducidad= findViewById<EditText>(R.id.et_fecha_producto).text.toString()

        val producto= Producto(
            id,
            nombre,
            precio,
            SimpleDateFormat("dd/MM/yyyy").parse(fechaCaducidad),
            idTienda

        )

        BDProducto.updateProducto(producto) {
            if (it) {
                mostrarSnackbar("Producto actualizado")
                val intent = Intent(this, VerProductosActivity::class.java)
                intent.putExtra("idTienda", idTienda)
                startActivity(intent)
            } else {
                mostrarSnackbar("Error al actualizar el producto")
            }
        }
    }

    fun mostrarSnackbar(texto: String) {
        Snackbar
            .make(
                findViewById(R.id.form_edit_producto), // view
                texto, // texto
                Snackbar.LENGTH_LONG // tiempo
            )
            .show()
    }

    fun irActividad(
        clase:Class<*>
    ){
        val intent = Intent(this, clase)
        startActivity(intent)
    }
}