package com.example.examen2b_sgpv_gr2

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
import com.example.examen2b_sgpv_gr2.database.BDTienda
import com.example.examen2b_sgpv_gr2.models.Tienda
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private var posicionItemSeleccionado = 0
    private lateinit var arregloTiendas: ArrayList<Tienda>
    private lateinit var adaptador: ArrayAdapter<Tienda>
    private lateinit var listViewTiendas: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arregloTiendas= ArrayList()
        adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, arregloTiendas)
        listViewTiendas = findViewById(R.id.lv_list_tienda)
        listViewTiendas.adapter = adaptador

        obtenerTiendas()
        registerForContextMenu(listViewTiendas)
        val btnCrearTienda = findViewById<Button>(R.id.btn_ir_crear_tienda)
        btnCrearTienda.setOnClickListener {
            irActividad(CreateTiendaActivity::class.java)
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?

    ){
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater=menuInflater
        inflater.inflate(R.menu.menu_tienda,menu)
        val info=menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion= info.position
        posicionItemSeleccionado=posicion
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar -> {
                val selectedItem = arregloTiendas[posicionItemSeleccionado]
                val intent= Intent(this,EditTiendaActivity::class.java)
                intent.putExtra("idTienda", selectedItem.id)
                startActivity(intent)
                return true
            }

            R.id.mi_eliminar -> {
                val selectedItem = arregloTiendas[posicionItemSeleccionado]
                abrirDialogo(selectedItem.id)
                return true
            }

            R.id.mi_ver_productos -> {
                val selectedItem = arregloTiendas[posicionItemSeleccionado]
                val intent = Intent(this, VerProductosActivity::class.java)
                intent.putExtra("idTienda", selectedItem.id)
                startActivity(intent)
                return true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    fun abrirDialogo(id: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Desea eliminar")
        builder.setPositiveButton(
            "Aceptar",
            DialogInterface.OnClickListener { dialog, which ->
                eliminarTienda(id)
                mostrarSnackbar("Tienda eliminada")
            }
        )

        builder.setNegativeButton(
            "Cancelar",
            null
        )

        val dialogo = builder.create()
        dialogo.show()
    }

    fun eliminarTienda(id:String){
        BDTienda.deleteTienda(id) {
            obtenerTiendas()
        }

        BDTienda.deleteProductosByTienda(id) {
            obtenerTiendas()
        }
    }
    private fun obtenerTiendas() {
        BDTienda.getAllTiendas{ tiendas ->
            actualizarListView(tiendas)
        }
    }

    private fun actualizarListView(nuevasTiendas: ArrayList<Tienda>) {
        arregloTiendas.clear()
        arregloTiendas.addAll(nuevasTiendas)
        adaptador.notifyDataSetChanged()
    }

    fun mostrarSnackbar(texto: String) {
        Snackbar
            .make(
                findViewById(R.id.id_layout_main), // view
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

}