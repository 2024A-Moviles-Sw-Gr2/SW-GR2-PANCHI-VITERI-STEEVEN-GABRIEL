package com.example.proyecto2b_sgpv_gr2.modelos

enum class Genero(
    val nombre: String,
    val id: Int,
) {
    ACCION("Acción", 1),
    AVENTURAS("Aventuras", 2),
    CARRERAS("Carreras", 3),
    DEPORTES("Deportes", 4),
    ESTRATEGIA("Estrategia", 5);

    companion object {
        fun obtenerPorId(id: Int): Genero? {
            return values().find { it.id == id }
        }
    }

    override fun toString(): String {
        return nombre
    }


}