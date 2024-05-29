package repositories

import models.Producto
import java.io.File
import java.text.SimpleDateFormat

class ProductoRepository( private val fileName:String) {
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy")

    fun guardarProducto(producto: Producto) {
        val registro = "${producto.id},${producto.nombre},${producto.precio}," +
                "${dateFormat.format(producto.fechaCaducidad)},${producto.tienda.id}\n"

        File(fileName).appendText(registro)

    }

    fun obtenerProducto(id: Int): Producto {
        val line = File(fileName).readLines().find { it.startsWith("$id,") }
        val campos = line?.split(",") ?: throw NoSuchElementException("No se encontro un producto con el ID $id")

        return Producto(
            id = campos[0].toInt(),
            nombre = campos[1],
            precio = campos[2].toDouble(),
            fechaCaducidad = dateFormat.parse(campos[3]),
            tienda = TiendaRepository(System.getProperty("user.dir")+ "\\Deber-01\\src\\main\\kotlin\\data\\Tiendas.txt").obtenerTienda(campos[4].toInt())
        )
    }

    fun obtenerProductos(): List<Producto> {
        return File(fileName).readLines().map{ line->
            val campos=line.split(",")
            Producto(
                id = campos[0].toInt(),
                nombre = campos[1],
                precio = campos[2].toDouble(),
                fechaCaducidad = dateFormat.parse(campos[3]),
                tienda = TiendaRepository(System.getProperty("user.dir")+ "\\Deber-01\\src\\main\\kotlin\\data\\Tiendas.txt").obtenerTienda(campos[4].toInt())
            )

        }
    }

    fun actualizarProducto(producto: Producto) {
        val lineas = File(fileName).readLines()
        val nuevaLista = lineas.map { line ->
            if (line.startsWith("${producto.id},")){
                    "${producto.id},${producto.nombre},${producto.precio}" +
                            "${dateFormat.format(producto.fechaCaducidad)},${producto.tienda.id}"


        }else{
            line
        }
    }
    File(fileName).writeText(nuevaLista.joinToString("\n"))
    }

    fun eliminarProducto(id: Int) {
        val lineas=File(fileName).readLines()
        val nuevaLista=lineas.filter{ !it.startsWith("$id,")}
        File(fileName).writeText(nuevaLista.joinToString("\n"))
    }

    fun obtenerProductosPorTienda(idTienda: Int): List<Producto> {
        val lineas= File(fileName).readLines()
        val productoTiendas=lineas
            .filter { it.split(",")[4].toInt() == idTienda }
            .map{crearProductoDesdeString(it)}
        return productoTiendas


    }

    private fun crearProductoDesdeString(linea:String):Producto{
        val tiendaRepository=TiendaRepository(System.getProperty("user.dir")+ "\\Deber-01\\src\\main\\kotlin\\data\\Tiendas.txt")
        val campos=linea.split(",")
        val id=campos[0].toInt()
        val nombre=campos[1]
        val precio=campos[2].toDouble()
        val fechaCaducidad=SimpleDateFormat("dd/MM/yyyy").parse(campos[3])
        val idTienda=campos[4].toInt()
        val tienda= tiendaRepository.obtenerTienda(idTienda)

        return Producto(id,nombre, precio,fechaCaducidad,tienda)


    }

}



