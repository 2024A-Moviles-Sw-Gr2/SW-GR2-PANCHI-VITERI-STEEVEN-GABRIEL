package repositories

import models.Tienda
import java.io.File

class TiendaRepository (private val fileName: String){

    fun guardarTienda(tienda: Tienda){
        val registro ="${tienda.id},${tienda.nombre},${tienda.telefono},${tienda.direccion}," +
                "${tienda.estaAbierto}\n"
        File(fileName).appendText(registro)
    }

    fun obtenerTienda(id: Int): Tienda{
        val line= File(fileName).readLines().find{it.startsWith("$id,")}
        val campos = line?.split(",") ?: throw NoSuchElementException("No se encontro una tienda con el ID $id")
        return Tienda(
            id=campos[0].toInt(),
            nombre=campos[1],
            telefono=campos[2],
            direccion=campos[3],
            estaAbierto = campos[4].toBoolean()

        )
    }

    fun obtenerTiendas(): List<Tienda>{
        return File(fileName).readLines().map{line->
            val campos= line.split(",")
            Tienda(
                id=campos[0].toInt(),
                nombre=campos[1],
                telefono=campos[2],
                direccion=campos[3],
                estaAbierto = campos[4].toBoolean()
            )
        }
    }

    fun actualizarTienda(tienda:Tienda){
        val lineas=File(fileName).readLines()
        val nuevaLista=lineas.map{ line->
            if(line.startsWith("${tienda.id}")){
                "${tienda.id},${tienda.nombre},${tienda.telefono},${tienda.direccion}," +
                        "${tienda.estaAbierto}"
            } else{
                line
            }
        }
        File(fileName).writeText(nuevaLista.joinToString("\n"))
    }

    fun eliminarTienda(id: Int){
        val productoRepository = ProductoRepository(System.getProperty("user.dir")+ "\\Deber-01\\src\\main\\kotlin\\data\\Productos.txt")
        //Obtener todos los productos

        val productosAsociados = productoRepository.obtenerProductosPorTienda(id)

        //Eliminar Tienda
        val lineas=File(fileName).readLines()

        val nuevaLista=lineas.filter{!it.startsWith("$id,")}
        File(fileName).writeText(nuevaLista.joinToString ("\n"))

        //Eliminar
        productosAsociados.forEach {productoRepository.eliminarProducto(it.id)}
    }
}