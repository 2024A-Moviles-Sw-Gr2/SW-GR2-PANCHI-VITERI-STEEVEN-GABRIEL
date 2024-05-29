package models
import repositories.TiendaRepository

class Tienda (
    val id: Int,
    val nombre: String,
    val telefono: String,
    val direccion: String,
    val estaAbierto:Boolean
){

    private val tiendaRepository= TiendaRepository(System.getProperty("user.dir")+ "\\Deber-01\\src\\main\\kotlin\\data\\Tiendas.txt")

    fun guardarTienda(){
        tiendaRepository.guardarTienda(this)
        println("Guardando tienda: $nombre")
    }

    fun obtenerTienda(id: Int):Tienda{
        return tiendaRepository.obtenerTienda(id)
    }

    fun obtenerTiendas():List<Tienda>{
        return tiendaRepository.obtenerTiendas()
    }

    fun actualizarTienda(){
        tiendaRepository.actualizarTienda(this)
        println("Actualizando tienda: $nombre")
    }

    fun eliminarTienda(){
        tiendaRepository.eliminarTienda(this.id)
        println("Eliminando tienda: $nombre")
    }


    override fun toString(): String {
        return  "ID Tienda: $id " +
                "Nombre: $nombre "+
                "Telefóno: $telefono "+
                "Dirección: $direccion "+
                "Abierto: $estaAbierto"

    }




}