package org.project.we3.data.datsource

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.project.we3.data.model.Camera
import org.project.we3.data.repository.CameraRepository

class CameraRepositoryImpl(
    private val firestore: FirebaseFirestore,
): CameraRepository {
    override suspend fun fetchCamera(): List<Camera> {
        var cameras = emptyList<Camera>()
        try {
            val snapshot = firestore.collection("CameraList").get().await()  // 🔥 Await the result

            cameras = snapshot.documents.map { document ->
                Camera(
                    firestoreId = document.id,  // 🔥 Store actual Firestore ID
                    name = document.getString("name") ?: "",
                    unitPrice = document.getDouble("unitPrice") ?: 0.0,
                    mrp = document.getDouble("mrp") ?: 0.0,
                    gst = document.getDouble("gst") ?: 0.0,
                    quantity = document.getLong("quantity")?.toInt() ?: 0
                )
            }
        } catch (e: Exception) {
            Log.e("CameraRepository", "Error fetching data from Firestore", e)
        }
        return cameras
    }

    override suspend fun addCamera(camera: Camera) {
        // Add camera to Firestore
        firestore.collection("CameraList")
            .add(camera)
            .await()
    }

    override suspend fun updateCamera(camera: Camera) {
        val cameraData = hashMapOf(
            "name" to camera.name,
            "mrp" to camera.mrp,
            "unitPrice" to camera.unitPrice,
            "gst" to camera.gst,
            "quantity" to camera.quantity
        )
        // Update camera in Firestore
        firestore.collection("CameraList")
            .document(camera.firestoreId)
            .set(cameraData)
            .await()
    }

    override suspend fun deleteCamera(cameraId: String) {
        // Delete camera from Firestore
        firestore.collection("CameraList").document(cameraId).delete().await()
    }

    override suspend fun updateQuantity(
        cameraId: String,
        quantity: Int
    ) {
        firestore
            .collection("CameraList")
            .document(cameraId)
            .update("quantity", quantity)
    }

}