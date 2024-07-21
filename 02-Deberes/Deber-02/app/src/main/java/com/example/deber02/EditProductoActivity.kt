package com.example.deber02

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.deber02.database.CrudProducto
import com.example.deber02.database.DBMemoria
import com.example.deber02.models.Producto
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat

class EditProductoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_producto)

        val idProducto=intent.getIntExtra("idProducto",0)
        val producto= DBMemoria.arregloProducto.find{ producto ->producto.id== idProducto}

        llenarInputs(producto!!)

        val btnActualizar= findViewById<Button>(R.id.btn_actualizar_producto)
        btnActualizar.setOnClickListener {
            actualizarProducto()
            mostrarSnackbar("Cancion actualizada")

            val intent = Intent(this, VerProductosActivity::class.java)
            intent.putExtra("id", producto.tienda.id.toString())
            startActivity(intent)
        }

        val btnBack=findViewById<Button>(R.id.btn_back_edit_producto)
        btnBack.setOnClickListener {
            val intent= Intent(this, VerProductosActivity::class.java)
            intent.putExtra("id", producto.tienda.id.toString())
            startActivity(intent)
        }
    }

    fun mostrarSnackbar(texto: String){
        Snackbar
            .make(
                findViewById(R.id.form_edit_producto),
                texto,
                Snackbar.LENGTH_LONG
            )
            .show()
    }

    fun llenarInputs(producto: Producto){
        val id=findViewById<EditText>(R.id.et_id_producto)
        val nombre=findViewById<EditText>(R.id.et_nombre_producto)
        val precio=findViewById<EditText>(R.id.et_precio_producto)
        val fechaCaducidad= findViewById<EditText>(R.id.et_fecha_producto)

        id.setText(producto.id.toString())
        nombre.setText(producto.nombre)
        precio.setText(producto.precio.toString())
        fechaCaducidad.setText(SimpleDateFormat("dd/MM/yyyy").format(producto.fechaCaducidad))

    }

    fun actualizarProducto(){
        val id=findViewById<EditText>(R.id.et_id_producto)
        val nombre=findViewById<EditText>(R.id.et_nombre_producto)
        val precio=findViewById<EditText>(R.id.et_precio_producto)
        val fechaCaducidad= findViewById<EditText>(R.id.et_fecha_producto)
        val idProducto=intent.getIntExtra("idProducto",0)
        val producto=DBMemoria.arregloProducto.find{producto->producto.id==idProducto}
        val idtienda= producto!!.tienda.id

        CrudProducto().editarProducto(
            id.text.toString().toInt(),
            nombre.text.toString(),
            precio.text.toString().toDouble(),
            SimpleDateFormat("dd/MM/yyyy").parse(fechaCaducidad.text.toString()),
            idtienda,

            )
    }

    fun irActividad(
        clase:Class<*>
    ){
        val intent = Intent(this, clase)
        startActivity(intent)
    }
}