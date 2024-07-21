package com.example.a2024aswgr2sgpv.models

class Tienda (

    var id:Int,
    var nombre: String,
    var telefono: String,
    var direccion: String,
    var estaAbierto: Boolean
        ){
    override fun toString():String{
        return "${nombre}"
    }


}