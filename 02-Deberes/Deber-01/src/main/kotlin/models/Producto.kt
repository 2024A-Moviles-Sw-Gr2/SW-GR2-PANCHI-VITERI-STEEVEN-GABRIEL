package models

import repositories.ProductoRepository
import java.text.SimpleDateFormat
import java.util.*

class Producto (
    val id: Int,
    val nombre: String,
    val precio: Double,
    val fechaCaducidad: Date,
    val tienda: Tienda

    ){

    private val productoRepository= ProductoRepository(System.getProperty("user.dir")+ "\\Deber-01\\src\\main\\kotlin\\data\\Productos.txt")

    fun guardarProducto(){
        productoRepository.guardarProducto(this)
        println("Guardando producto: $nombre")
    }

    fun obtenerProducto(id: Int):Producto{
        return productoRepository.obtenerProducto(id)
    }

    fun obtenerProductos():List<Producto>{
        return productoRepository.obtenerProductos()
    }

    fun actualizarProducto(){
        productoRepository.actualizarProducto(this)
        println("Actualizando producto: $nombre")
    }

    fun eliminarProducto(){
        productoRepository.eliminarProducto(this.id)
        println("Eliminando producto: $nombre")
    }

    override fun toString(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        return "ID Producto: $id " +
                "Nombre: $nombre " +
                "Precio: $precio " +
                "Fecha Caducidad: ${dateFormat.format(fechaCaducidad)} " +
                "Tienda: $tienda"
    }
}