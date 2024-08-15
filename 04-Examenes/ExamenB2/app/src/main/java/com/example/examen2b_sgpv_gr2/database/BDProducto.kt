package com.example.examen2b_sgpv_gr2.database

import com.example.examen2b_sgpv_gr2.models.Producto
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class BDProducto {
    companion object {
        fun getProductosByTienda(idTienda: String, callback: (ArrayList<Producto>) -> Unit) {
            val productos = ArrayList<Producto>()
            val db = Firebase.firestore

            db.collection("Productos")
                .whereEqualTo("idTienda", idTienda)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        println("${document.id} => ${document.data}")
                        val producto = Producto(
                            document.id,
                            document.data["nombre"].toString(),
                            document.data["precio"].toString().toDouble(),
                            (document.data["fechaCaducidad"] as com.google.firebase.Timestamp).toDate(),
                            document.data["idTienda"].toString()
                        )
                        productos.add(producto)
                    }
                    callback(productos)
                }
                .addOnFailureListener { exception ->
                    println("Error getting documents: $exception")
                    callback(ArrayList())
                }
        }

        fun createProducto(producto: Producto, callback: (Boolean) -> Unit) {
            val db = Firebase.firestore

            val productoDoc = hashMapOf(
                "nombre" to producto.nombre,
                "precio" to producto.precio,
                "fechaCaducidad" to producto.fechaCaducidad,
                "idTienda" to producto.idTienda

            )

            db.collection("Productos")
                .add(productoDoc)
                .addOnSuccessListener { documentReference ->
                    println("DocumentSnapshot added with ID: ${documentReference.id}")
                    callback(true)
                }
                .addOnFailureListener { e ->
                    println("Error adding document: $e")
                    callback(false)
                }
        }

        fun getProductoById(idProducto: String, callback: (Producto?) -> Unit) {
            val db = Firebase.firestore

            db.collection("Productos")
                .document(idProducto)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        println("DocumentSnapshot data: ${document.data}")
                        val producto = Producto(
                            document.id,
                            document.data?.get("nombre").toString(),
                            document.data?.get("precio").toString().toDouble(),
                            (document.data?.get("fechaCaducidad") as com.google.firebase.Timestamp).toDate(),
                            document.data?.get("idTienda").toString()
                        )
                        callback(producto)
                    } else {
                        println("No such document")
                        callback(null)
                    }
                }
                .addOnFailureListener { exception ->
                    println("get failed with $exception")
                    callback(null)
                }
        }

        fun updateProducto(producto: Producto, callback: (Boolean) -> Unit) {
            val db = Firebase.firestore

            val productoDoc = hashMapOf(

                "nombre" to producto.nombre,
                "precio" to producto.precio,
                "fechaCaducidad" to producto.fechaCaducidad,
                "idTienda" to producto.idTienda


            )

            db.collection("Productos")
                .document(producto.id)
                .set(productoDoc)
                .addOnSuccessListener {
                    println("DocumentSnapshot successfully updated!")
                    callback(true)
                }
                .addOnFailureListener { e ->
                    println("Error updating document: $e")
                    callback(false)
                }
        }

        fun deleteProducto(idProducto: String, callback: (Boolean) -> Unit) {
            val db = Firebase.firestore

            db.collection("Productos")
                .document(idProducto)
                .delete()
                .addOnSuccessListener {
                    println("DocumentSnapshot successfully deleted!")
                    callback(true)
                }
                .addOnFailureListener { e ->
                    println("Error deleting document: $e")
                    callback(false)
                }
        }
    }




    }