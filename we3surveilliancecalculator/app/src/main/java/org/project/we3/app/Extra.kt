package org.project.we3.app

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


fun uploadData(cameras: List<Camera>) {
    // Upload data to Firestore
    val db = FirebaseFirestore.getInstance()
    for (camera in cameras) {
        db.collection("CameraList")
            .add(
                hashMapOf(
                    "name" to camera.name,
                    "unitPrice" to camera.unitPrice,
                    "detail" to camera.detail,
                    "mrp" to camera.mrp,
                    "gst" to camera.gst,
                    "quantity" to camera.quantity
                )
            ).addOnSuccessListener {
                Log.d("MainActivity1", "DocumentSnapshot added with ID: $it")
            }.addOnFailureListener {
                Log.w("MainActivity1", "Error adding document", it)
            }
    }
}

fun removeDuplicates() {
    val db = FirebaseFirestore.getInstance()
    val cameraCollection = db.collection("CameraList")

    cameraCollection.get().addOnSuccessListener { snapshot ->
        if (snapshot != null && !snapshot.isEmpty) {
            val cameraMap = mutableMapOf<String, MutableList<DocumentSnapshot>>()

            // Group documents by a unique key (e.g., "name")
            for (document in snapshot.documents) {
                val cameraName = document.getString("name") ?: continue
                cameraMap.getOrPut(cameraName) { mutableListOf() }.add(document)
            }

            // Iterate over grouped cameras and delete duplicates
            for ((_, documents) in cameraMap) {
                if (documents.size > 1) {
                    // Keep the first document and delete the rest
                    val documentsToDelete = documents.drop(1)  // Drop first, keep the rest
                    for (doc in documentsToDelete) {
                        doc.reference.delete()
                            .addOnSuccessListener { Log.d("Firestore", "Deleted duplicate: ${doc.id}") }
                            .addOnFailureListener { e -> Log.e("Firestore", "Error deleting duplicate", e) }
                    }
                }
            }
        } else {
            Log.d("Firestore", "No data found")
        }
    }.addOnFailureListener { e ->
        Log.e("Firestore", "Error fetching documents", e)
    }
}

fun getData(): StateFlow<List<Camera>> {
    val cameras = MutableStateFlow<List<Camera>>(emptyList())
    val db = FirebaseFirestore.getInstance()

    db.collection("CameraList")
        .orderBy("name")
        .addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("Firestore", "Error fetching camera: ${exception.message}")
                return@addSnapshotListener
            }
            val cameraList = snapshot?.toObjects(Camera::class.java) ?: emptyList()
            Log.d("Firestore", "Fetched ${cameraList.size} cameras")
            cameras.value = cameraList  // Update StateFlow
        }

    return cameras.asStateFlow() // Correct way to return StateFlow
}