package com.example.proyecto2b_sgpv_gr2.modelos

import java.io.Serializable
import java.time.LocalDate

class Resenia (
    val usuario: Usuario,
    val fecha: LocalDate,
    val comentario: String,
    val calificacion: Double
): Serializable {
    override fun toString(): String {
        return "Reseña(usuario='$usuario', fecha='$fecha', comentario='$comentario')"
    }
}