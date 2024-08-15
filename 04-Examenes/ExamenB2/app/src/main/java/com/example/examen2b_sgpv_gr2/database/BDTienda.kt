package com.example.examen2b_sgpv_gr2.database

import com.example.examen2b_sgpv_gr2.models.Tienda
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class BDTienda {

    companion object {
        fun getAllTiendas(callback: (ArrayList<Tienda>) -> Unit) {
            val tiendas = ArrayList<Tienda>()
            val db = Firebase.firestore

            db.collection("Tiendas")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        println("${document.id} => ${document.data}")
                        val tienda = Tienda(
                            document.id,
                            document.data["nombre"].toString(),
                            document.data["telefono"].toString(),
                            document.data["direccion"].toString(),
                            document.data["estaAbierto"].toString().toBoolean()
                        )
                        tiendas.add(tienda)
                    }
                    callback(tiendas)
                }
                .addOnFailureListener { exception ->
                    println("Error getting documents: $exception")
                    callback(ArrayList()) // Manejo de errores, devolver una lista vacía
                }
        }

        fun createTienda(tiendaP: Tienda, callback: (Boolean) -> Unit) {
            val db = Firebase.firestore
            val tienda = hashMapOf(
                "nombre" to tiendaP.nombre,
                "telefono" to tiendaP.telefono,
                "direccion" to tiendaP.direccion,
                "estaAbierto" to tiendaP.estaAbierto

            )

            db.collection("Tiendas")
                .add(tienda)
                .addOnSuccessListener { documentReference ->
                    println("DocumentSnapshot added with ID: ${documentReference.id}")
                    callback(true)
                }
                .addOnFailureListener { e ->
                    println("Error adding document: $e")
                    callback(false)
                }
        }

        fun updateTienda(tiendaP: Tienda, callback: (Boolean) -> Unit) {
            val db = Firebase.firestore
            val tienda = hashMapOf(
                "nombre" to tiendaP.nombre,
                "telefono" to tiendaP.telefono,
                "direccion" to tiendaP.direccion,
                "estaAbierto" to tiendaP.estaAbierto
            )

            db.collection("Tiendas")
                .document(tiendaP.id)
                .set(tienda)
                .addOnSuccessListener {
                    println("DocumentSnapshot successfully updated!")
                    callback(true)
                }
                .addOnFailureListener { e ->
                    println("Error updating document: $e")
                    callback(false)
                }
        }

        fun getTiendaById(idTienda: String, callback: (Tienda?) -> Unit) {
            val db = Firebase.firestore
            db.collection("Tiendas")
                .document(idTienda)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        println("DocumentSnapshot data: ${document.data}")
                        val tienda = Tienda(
                            document.id,
                            document.data?.get("nombre").toString(),
                            document.data?.get("telefono").toString(),
                            document.data?.get("direccion").toString(),
                            document.data?.get("estaAbierto").toString().toBoolean()
                        )
                        callback(tienda)
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

        fun deleteTienda(idTienda: String, callback: (Boolean) -> Unit) {
            val db = Firebase.firestore
            db.collection("Tiendas")
                .document(idTienda)
                .delete()
                .addOnSuccessListener {
                    callback(true)
                }
                .addOnFailureListener {
                    callback(false)
                }
        }


        fun deleteProductosByTienda(idTienda: String, callback: (Boolean) -> Unit) {
            val db = Firebase.firestore
            db.collection("Productos")
                .whereEqualTo("idTienda", idTienda)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        db.collection("Tiendas")
                            .document(document.id)
                            .delete()
                            .addOnSuccessListener {
                                callback(true)
                            }
                            .addOnFailureListener {
                                callback(false)
                            }
                    }
                }
                .addOnFailureListener {
                    callback(false)
                }
        }
    }








    }