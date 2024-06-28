package com.example.a2024aswgr2sgpv

class BBaseDatosMemoria {
    companion object{
        val arregloBEntrenador= arrayListOf<BEntrenador>()

        init{
            arregloBEntrenador
                .add(
                    BEntrenador(1,"Steeven","a@a.com")
                )
            arregloBEntrenador
                .add(
                    BEntrenador(1,"Samantha","b@b.com")
                )

            arregloBEntrenador
                .add(
                    BEntrenador(1,"Gabriel","c@c.com")
                )

        }
    }

}