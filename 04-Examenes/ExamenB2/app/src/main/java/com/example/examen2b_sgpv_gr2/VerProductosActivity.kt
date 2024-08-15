package com.example.examen2b_sgpv_gr2

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
import com.example.examen2b_sgpv_gr2.database.BDProducto
import com.example.examen2b_sgpv_gr2.models.Producto

import com.google.android.material.snackbar.Snackbar


class VerProductosActivity : AppCompatActivity() {

    private var posicionItemSeleccionado = 0
    private lateinit var arregloProductos: ArrayList<Producto>
    private lateinit var listViewProductos: ListView
    private lateinit var adaptador: ArrayAdapter<Producto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_productos)

        val idTienda = intent.getStringExtra("idTienda")

        if (idTienda != null) {
            arregloProductos = ArrayList()
            listViewProductos = findViewById(R.id.lv_list_productos)
            adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, arregloProductos)
            listViewProductos.adapter = adaptador
            obtenerProductosDesdeFirestore(idTienda)
            registerForContextMenu(listViewProductos)

            val btn_crear_producto = findViewById<Button>(R.id.btn_ir_crear_producto)
            btn_crear_producto.setOnClickListener {
                val intent = Intent(this, CreateProductoActivity::class.java)

                intent.putExtra("idTienda", idTienda)

                startActivity(intent)
            }

            val btn_ir_tiendas = findViewById<View>(R.id.btn_back_ver_producto)
            btn_ir_tiendas.setOnClickListener {
                irActividad(MainActivity::class.java)
            }


        } else {
            mostrarSnackbar("No se ha encontrado el id de la tienda")
            irActividad(MainActivity::class.java)
        }
    }

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
                intent.putExtra("idProducto", arregloProductos[posicionItemSeleccionado].id)

                startActivity(intent)
                return true
            }

            R.id.mi_eliminar -> {
                var idProducto=arregloProductos[posicionItemSeleccionado].id
                abrirDialogo(idProducto)
                return true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    private fun obtenerProductosDesdeFirestore(idTienda:String) {
        BDProducto.getProductosByTienda(idTienda) { productos ->
            actualizarListaProductos(productos)
        }
    }

    private fun actualizarListaProductos(nuevasProductos: ArrayList<Producto>) {
        adaptador.clear()
        adaptador.addAll(nuevasProductos)
        adaptador.notifyDataSetChanged()
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

    fun eliminarProducto(id: String) {
        BDProducto.deleteProducto(id) {
            if (it) {
                val idTienda = intent.getStringExtra("idTienda")
                if (idTienda != null) {
                    obtenerProductosDesdeFirestore(idTienda)
                }
            } else {
                mostrarSnackbar("Error al eliminar el producto")
            }
        }
    }


    fun abrirDialogo(id: String) {
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







