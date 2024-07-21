package com.example.deber02.database

import com.example.deber02.models.Producto
import com.example.deber02.models.Tienda
import java.text.SimpleDateFormat

class DBMemoria {

    companion object {
        val arregloTienda=arrayListOf<Tienda>()
        val arregloProducto= arrayListOf<Producto>()

        init{
            //Registros para el modelo Tienda

            arregloTienda.add(
                Tienda(
                    1,
                    "Supermaxi",
                    "268732090",
                    "Centro Comercial El Recreo",
                    true
                )

            )

            arregloTienda.add(
                Tienda(
                    2,
                    "Santa María",
                    "268728929",
                    "Chillogallo",
                    false
                )

            )

            arregloTienda.add(
                Tienda(
                    3,
                    "Gran Aki",
                    "268732882",
                    "Centro Comercial Atahualpa",
                    true
                )

            )

            //Registros para el Producto

            arregloProducto.add(
                Producto(
                    1,
                    "Detergente",
                    3.50,
                    SimpleDateFormat("dd/MM/yyyy").parse("13/11/2025"),
                    arregloTienda.find{ tienda ->
                        tienda.id == 1}!!
                )
            )

            arregloProducto.add(
                Producto(
                    2,
                    "Leche",
                    1.80,
                    SimpleDateFormat("dd/MM/yyyy").parse("17/09/2024"),
                    arregloTienda.find{ tienda ->
                        tienda.id == 2}!!
                )
            )

            arregloProducto.add(
                Producto(
                    3,
                    "Cereal",
                    5.60,
                    SimpleDateFormat("dd/MM/yyyy").parse("06/12/2024"),
                    arregloTienda.find{ tienda ->
                        tienda.id == 3}!!
                )
            )



        }
    }
}