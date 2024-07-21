package com.example.deber02.database

import com.example.deber02.models.Tienda

class CrudTienda {
    fun crearTienda(
        id:Int,
        nombre:String,
        telefono:String,
        direccion:String,
        estaAbierto: Boolean
    ){
        val tienda= Tienda(
            id,
            nombre,
            telefono,
            direccion,
            estaAbierto
        )
        DBMemoria.arregloTienda.add(tienda)

    }
    fun editarTienda(
        id:Int,
        nombre:String,
        telefono:String,
        direccion:String,
        estaAbierto: Boolean
    ){
        val tienda= Tienda(
            id,
            nombre,
            telefono,
            direccion,
            estaAbierto
        )

        val tiendaAux= DBMemoria.arregloTienda.find{ tienda ->
            tienda.id==id
        }

        val posicion = DBMemoria.arregloTienda.indexOf(tiendaAux)
        DBMemoria.arregloTienda[posicion]=tienda
    }
    fun eliminarProductosTienda(id: Int){
        val productos= DBMemoria.arregloProducto.filter {producto->
            producto.tienda.id == id
        }
        productos.forEach { producto->
            DBMemoria.arregloProducto.removeIf{productoAux->
                productoAux.id ==producto.id
            }
        }
    }
}