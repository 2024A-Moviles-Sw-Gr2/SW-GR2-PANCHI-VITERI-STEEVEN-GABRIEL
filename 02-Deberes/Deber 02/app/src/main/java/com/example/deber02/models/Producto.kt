package com.example.deber02.models

import java.util.Date

class Producto (
    var id:Int,
    var nombre: String,
    var precio: Double,
    var fechaCaducidad: Date,
    var tienda: Tienda
){
    override fun toString():String{
        return "${nombre}"
    }

}