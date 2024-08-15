package com.example.proyecto2b_sgpv_gr2.modelos

import java.io.Serializable
import java.time.LocalDate

class Videojuego(
    var id: String,
    var titulo: String,
    var calificacion: Double,
    var generos: Array<Genero?>,
    var imagen: String,
    var plataforma: String,
    var fecha: LocalDate,
    var resenias: ArrayList<Resenia>,
    var elenco: Array<String>,
    var resumen: String

) :Serializable {

    override fun toString(): String {
        return "Videojuego(titulo='$titulo')"
    }

}