package com.example.deber02

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import com.example.deber02.database.CrudTienda
import com.example.deber02.database.DBMemoria
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    val arregloTienda= DBMemoria.arregloTienda
    var posicionItemSeleccionado=-1

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?

    ){
        super.onCreateContextMenu(menu, v, menuInfo)
        //Llenamos las opciones del menu
        val inflater=menuInflater
        inflater.inflate(R.menu.menu,menu)
        //Obtener el id del ArrayListSeleccionado
        val info=menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion= info.position
        posicionItemSeleccionado=posicion
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar -> {
                val selectedItem = arregloTienda[posicionItemSeleccionado]
                //Crear un Intent para iniciar VerProductosActivity
                val intent= Intent(this,EditTiendaActivity::class.java)
                //Pasar el parametro "nombre" al Intent
                intent.putExtra("id", selectedItem.id.toString())
                //Iniciar la actividad VerProductosActivity

                startActivity(intent)
                return true
            }

            R.id.mi_eliminar -> {
                abrirDialogo(posicionItemSeleccionado)
                return true
            }

            R.id.mi_ver_productos -> {
                val selectedItem = arregloTienda[posicionItemSeleccionado]
                // Crear un Intent para iniciar VerCancionesActivity
                val intent = Intent(this, VerProductosActivity::class.java)

                // Pasar el parámetro "nombre" al Intent
                intent.putExtra("id", selectedItem.id.toString())

                // Iniciar la actividad VerCancionesActivity
                startActivity(intent)
                return true
            }

            else -> super.onContextItemSelected(item)
        }
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView = findViewById<ListView>(R.id.lv_list_tienda)
        val adaptador = ArrayAdapter(
            this, // Contexto
            android.R.layout.simple_list_item_1, // como se va a ver (XML)
            arregloTienda
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()

        registerForContextMenu(listView)

        val btnCrearTienda = findViewById<View>(R.id.btn_ir_crear_tienda)
        btnCrearTienda.setOnClickListener {
            irActividad(CreateTiendaActivity::class.java)
        }
    }

    fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }

    fun eliminarTienda(id:Int){
        val listView = findViewById<ListView>(R.id.lv_list_tienda)
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arregloTienda
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()

        var idTiendaAEliminar = arregloTienda[id].id
        CrudTienda().eliminarProductosTienda(idTiendaAEliminar)
        arregloTienda.removeAt(
            id
        )
    }

    fun abrirDialogo(id: Int) {
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
}