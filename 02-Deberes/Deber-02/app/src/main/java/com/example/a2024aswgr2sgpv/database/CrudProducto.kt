package com.example.a2024aswgr2sgpv.database

import com.example.a2024aswgr2sgpv.models.Producto
import java.util.Date

class CrudProducto {
    fun crearProducto(
        id: Int,
        nombre: String,
        precio: Double,
        fechaCaducidad: Date,
        idTienda: Int

    ){
        val producto= Producto(
            id,
            nombre,
            precio,
            fechaCaducidad,
            DBMemoria.arregloTienda.find{ tienda -> tienda.id ==idTienda}!!
        )
        DBMemoria.arregloProducto.add(producto)
    }

    fun editarProducto(
        id: Int,
        nombre: String,
        precio: Double,
        fechaCaducidad: Date,
        idTienda: Int
    ){
        val producto= Producto(
            id,
            nombre,
            precio,
            fechaCaducidad,
            DBMemoria.arregloTienda.find{ tienda -> tienda.id ==idTienda}!!
        )
        var posicion = -1

        DBMemoria.arregloProducto.forEachIndexed{index, producto->
            if(producto.id == id){
                posicion=index
            }
        }
        DBMemoria.arregloProducto[posicion]=producto
    }
}