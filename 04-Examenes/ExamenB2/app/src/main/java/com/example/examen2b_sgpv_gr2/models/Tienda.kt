package com.example.examen2b_sgpv_gr2.models

class Tienda (

    var id:String,
    var nombre: String,
    var telefono: String,
    var direccion: String,
    var estaAbierto: Boolean
){
    override fun toString():String{
        return "${nombre} "
    }


}