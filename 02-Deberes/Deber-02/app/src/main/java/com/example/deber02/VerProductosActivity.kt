package com.example.deber02

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import com.example.deber02.database.DBMemoria
import com.example.deber02.models.Producto
import com.google.android.material.snackbar.Snackbar


class VerProductosActivity : AppCompatActivity() {

    val arregloProductos=DBMemoria.arregloProducto
    var posicionItemSeleccionado=-1
    var arregloProductoporTienda=arrayListOf<Producto>()

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?

    ){
        super.onCreateContextMenu(menu, v, menuInfo)
        //Llenamos las opciones del menu
        val inflater=menuInflater
        inflater.inflate(R.menu.menu_producto,menu)
        //Obtener el id del ArrayListSeleccionado
        val info=menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion= info.position
        posicionItemSeleccionado=posicion
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar -> {
                val intent = Intent(this, EditProductoActivity::class.java)
                intent.putExtra("idProducto", arregloProductoporTienda[posicionItemSeleccionado].id)

                startActivity(intent)
                return true
            }

            R.id.mi_eliminar -> {
                abrirDialogo(posicionItemSeleccionado)
                return true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_productos)
        val idTienda = intent.getStringExtra("id")
        mostrarSnackbar("Ver productos de: $idTienda")

        arregloProductoporTienda=
            arregloProductos.filter { producto -> producto.tienda.id == Integer.parseInt(idTienda) } as ArrayList<Producto>

        val listView = findViewById<ListView>(R.id.lv_list_productos)
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arregloProductoporTienda
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()
        registerForContextMenu(listView)

        val btnCrearProducto = findViewById<Button>(R.id.btn_ir_crear_producto)
        btnCrearProducto.setOnClickListener {
            // Crear un Intent para iniciar CrearProductoActivity
            val intentCreate = Intent(this, CreateProductoActivity::class.java)

            // Pasar el parámetro "nombre" al Intent
            intentCreate.putExtra("idTienda", idTienda)
            // Iniciar la actividad VerCancionesActivity
            startActivity(intentCreate)
        }

        val btnBack = findViewById<Button>(R.id.btn_back_ver_producto)
        btnBack.setOnClickListener {
            irActividad(MainActivity::class.java)
        }
    }

    fun mostrarSnackbar(texto: String) {
        Snackbar
            .make(
                findViewById(R.id.id_layouts_intents), // view
                texto, // texto
                Snackbar.LENGTH_LONG // tiempo
            )
            .show()
    }

    fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }

    fun eliminarProducto(id: Int) {

        val listView = findViewById<ListView>(R.id.lv_list_productos)

        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arregloProductoporTienda
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()

        val idProductoAEliminar = arregloProductoporTienda[id].id
        arregloProductoporTienda.removeAt(id)

        DBMemoria.arregloProducto.removeIf { producto -> producto.id == idProductoAEliminar }
    }

    fun abrirDialogo(id: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Desea eliminar")
        builder.setPositiveButton(
            "Aceptar",
            DialogInterface.OnClickListener { dialog, which ->
                eliminarProducto(id)
                mostrarSnackbar("Producto eliminado")
            }
        )

        builder.setNegativeButton(
            "Cancelar",
            null
        )

        val dialogo = builder.create()
        dialogo.show()
    }

}