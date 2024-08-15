package com.example.examen2b_sgpv_gr2.models


import java.util.Date

class Producto (
    var id:String,
    var nombre: String,
    var precio: Double,
    var fechaCaducidad: Date,
    var idTienda: String
){
    override fun toString():String{
        return "${nombre}"
    }

}