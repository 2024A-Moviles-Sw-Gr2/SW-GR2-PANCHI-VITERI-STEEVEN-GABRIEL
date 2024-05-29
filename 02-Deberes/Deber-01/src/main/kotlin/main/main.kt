package main
import models.Tienda
import models.Producto
import repositories.ProductoRepository
import repositories.TiendaRepository
import java.text.SimpleDateFormat
import java.util.*

fun main() {

    val tiendaRepository= TiendaRepository(System.getProperty("user.dir")+ "\\Deber-01\\src\\main\\kotlin\\data\\Tiendas.txt")
    val productoRepository= ProductoRepository(System.getProperty("user.dir")+ "\\Deber-01\\src\\main\\kotlin\\data\\Productos.txt")

    val scanner = Scanner(System.`in`)
    var opcion: Int

    do {
        println("=== Menú CRUD ===")
        println("1. Agregar Tienda")
        println("2. Agregar Producto")
        println("3. Mostrar Tiendas")
        println("4. Mostrar Productos")
        println("5. Actualizar Tienda")
        println("6. Actualizar Producto")
        println("7. Eliminar Tienda")
        println("8. Eliminar Producto")
        println("9. Salir")

        print("Seleccione una opción:")
        opcion = scanner.nextInt()

        when (opcion) {
            1 -> {
                agregarTienda(tiendaRepository)

            }
            2 -> {
                agregarProducto(tiendaRepository, productoRepository)
            }
            3 -> {
                mostrarTiendas(tiendaRepository)
            }
            4 -> {
                mostrarProductos(productoRepository)
            }
            5 -> {
                actualizarTienda(tiendaRepository)
            }
            6 -> {
                actualizarProducto(tiendaRepository, productoRepository)
            }
            7 -> {
                eliminarTienda(tiendaRepository)
            }
            8 -> {
                eliminarProducto(productoRepository)
            }
            9 -> {
                println("Saliendo de la aplicación.")
            }
            else -> println("Opcion no valida. Intentalo nuevamente")

        }


    }while(opcion != 9)
}

fun agregarTienda(tiendaRepository: TiendaRepository) {
    println("=== Agregar Tienda ===")
    print("ID Tienda: ")
    val id = readLine()?.toInt() ?: 0

    print("Nombre: ")
    val nombre = readLine() ?: ""

    print("Telefono: ")
    val telefono = readLine() ?: ""

    print("Direccion: ")
    val direccion = readLine()?: ""

    print("Esta Abierto (true/false): ")
    val estaAbierto = readLine()?.toBoolean() ?: false

    val nuevaTienda = Tienda(id, nombre, telefono, direccion, estaAbierto)
    tiendaRepository.guardarTienda(nuevaTienda)

    println("Tienda agregada correctamente.\n")
}

fun agregarProducto(tiendaRepository: TiendaRepository, productoRepository: ProductoRepository ) {
    println("=== Agregar Producto ===")
    print("ID Producto: ")
    val id = readLine()?.toInt() ?: 0

    print("Nombre: ")
    val nombre = readLine() ?: ""

    print("Precio: ")
    val precio = readLine()?.toDouble() ?: 0.0

    print("Fecha de Caducidad (dd/MM/yyyy): ")
    val fechaCaducidadStr = readLine() ?: ""
    val fechaCaducidad = SimpleDateFormat("dd/MM/yyyy").parse(fechaCaducidadStr)

    print("ID de la Tienda: ")
    val idTienda = readLine()?.toInt() ?: 0
    val tienda = tiendaRepository.obtenerTienda(idTienda)

    val nuevoProducto = Producto(id, nombre, precio, fechaCaducidad, tienda)
    productoRepository.guardarProducto(nuevoProducto)

    println("Producto agregado correctamente.\n")
}

fun mostrarTiendas(tiendaRepository: TiendaRepository){
    println("=== Tiendas ===")
    val tiendas = tiendaRepository.obtenerTiendas()
    tiendas.forEach { println(it) }
    println()
}

fun mostrarProductos(productoRepository: ProductoRepository) {
    println("=== Productos ===")
    val productos = productoRepository.obtenerProductos()
    productos.forEach { println(it) }
    println()
}

fun actualizarTienda(tiendaRepository: TiendaRepository) {
    println("=== Actualizar Tienda ===")
    // Capturar ID de la tienda a actualizar
    print("Ingrese el ID de la Tienda a actualizar: ")
    val idActualizar = readLine()?.toIntOrNull()

    if (idActualizar != null) {
        // Verificar si la tienda existe
        val tiendaExistente = tiendaRepository.obtenerTienda(idActualizar)

        // Si la tienda existe, permitir la actualización
        if (tiendaExistente != null) {
            // Capturar nuevos datos del usuario...
            print("Nuevo nombre (Enter para mantener el actual '${tiendaExistente.nombre}'): ")
            val nuevoNombre = readLine()?.takeIf { it.isNotBlank() } ?: tiendaExistente.nombre

            print("Nuevo telefono (Enter para mantener el actual '${tiendaExistente.telefono}'): ")
            val nuevoTelefono = readLine()?.takeIf { it.isNotBlank() } ?: tiendaExistente.telefono

            print("Nueva direccion (Enter para mantener el actual '${tiendaExistente.direccion}'): ")
            val nuevaDireccion = readLine()?.takeIf { it.isNotBlank() } ?: tiendaExistente.direccion

            print("Esta abierto (true/false) (Enter para mantener el actual '${tiendaExistente.estaAbierto}'): ")
            val nuevaEstaAbierto = readLine()?.toBoolean() ?: tiendaExistente.estaAbierto

            val tiendaActualizada = Tienda(idActualizar, nuevoNombre, nuevoTelefono,nuevaDireccion,nuevaEstaAbierto)
            tiendaRepository.actualizarTienda(tiendaActualizada)
            println("Tienda actualizada correctamente.\n")
        } else {
            println("No se encontró una Tienda con el ID proporcionado.\n")
        }
    } else {
        println("Entrada no válida. El ID debe ser un número entero.\n")
    }
}

fun actualizarProducto(tiendaRepository: TiendaRepository, productoRepository: ProductoRepository) {
    println("=== Actualizar Producto ===")
    // Capturar ID del Producto a actualizar
    print("Ingrese el ID del Producto a actualizar: ")
    val idActualizar = readLine()?.toIntOrNull()

    if (idActualizar != null) {
        // Verificar si el producto existe
        val productoExistente = productoRepository.obtenerProducto(idActualizar)

        // Si la canción existe, permitir la actualización
        if (productoExistente != null) {
            // Capturar nuevos datos del usuario...
            print("Nuevo nombre (Enter para mantener el actual '${productoExistente.nombre}'): ")
            val nuevoNombre = readLine()?.takeIf { it.isNotBlank() } ?: productoExistente.nombre

            print("Nuevo precio (Enter para mantener el actual '${productoExistente.precio}'): ")
            val nuevoPrecio = readLine()?.toDouble()?: productoExistente.precio

            print("Nueva fecha de Caducidad (dd/MM/yyyy) (Enter para mantener la actual '${SimpleDateFormat("dd/MM/yyyy").format(productoExistente.fechaCaducidad)}'): ")
            val nuevaFechaCaducidadStr = readLine()
            val nuevaFechaCaducidad = if (nuevaFechaCaducidadStr.isNullOrBlank()) productoExistente.fechaCaducidad else SimpleDateFormat("dd/MM/yyyy").parse(nuevaFechaCaducidadStr)


            print("Nuevo ID de la Tienda (Enter para mantener el actual '${productoExistente.tienda.id}'): ")
            val nuevoIdTienda = readLine()?.toIntOrNull() ?: productoExistente.tienda.id
            val nuevaTienda = tiendaRepository.obtenerTienda(nuevoIdTienda)

            val productoActualizado = Producto(idActualizar, nuevoNombre, nuevoPrecio,nuevaFechaCaducidad,nuevaTienda)
            productoRepository.actualizarProducto(productoActualizado)
            println("Producto actualizado correctamente.\n")
        } else {
            println("No se encontró un Producto con el ID proporcionado.\n")
        }
    } else {
        println("Entrada no válida. El ID debe ser un número entero.\n")
    }
}

fun eliminarTienda(tiendaRepository: TiendaRepository) {
    println("=== Eliminar Tienda ===")
    // Capturar ID de la tienda a eliminar
    print("Ingrese el ID de la Tienda a eliminar: ")
    val idEliminar = readLine()?.toIntOrNull()

    if (idEliminar != null) {
        // Verificar si el género existe
        val tiendaExistente = tiendaRepository.obtenerTienda(idEliminar)

        // Si la tienda existe, permitir la eliminación
        if (tiendaExistente != null) {
            tiendaRepository.eliminarTienda(idEliminar)
            println("Tienda eliminada correctamente.\n")
        } else {
            println("No se encontró una Tienda con el ID proporcionado.\n")
        }
    } else {
        println("Entrada no válida. El ID debe ser un número entero.\n")
    }
}

fun eliminarProducto(productoRepository: ProductoRepository) {
    println("=== Eliminar Producto ===")
    // Capturar ID del producto a eliminar
    print("Ingrese el ID del Producto a eliminar: ")
    val idEliminar = readLine()?.toIntOrNull()

    if (idEliminar != null) {
        // Verificar si el producto existe
        val productoExistente = productoRepository.obtenerProducto(idEliminar)

        // Si el producto existe, permitir la eliminación
        if (productoExistente != null) {
            productoRepository.eliminarProducto(idEliminar)
            println("Producto eliminado correctamente.\n")
        } else {
            println("No se encontró un Producto con el ID proporcionado.\n")
        }
    } else {
        println("Entrada no válida. El ID debe ser un número entero.\n")
    }
}
