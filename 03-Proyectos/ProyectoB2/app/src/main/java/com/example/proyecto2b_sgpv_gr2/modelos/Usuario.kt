package com.example.proyecto2b_sgpv_gr2.modelos

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class Usuario(
    val nombre: String,
    val correo: String? = null
) : Serializable {

    override fun toString(): String {
        return nombre
    }
}