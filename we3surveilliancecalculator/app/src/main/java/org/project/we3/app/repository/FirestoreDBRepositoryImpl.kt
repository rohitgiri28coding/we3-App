package org.project.we3.app.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.project.we3.app.Camera
import org.project.we3.app.db.CameraViewModel
import org.project.we3.app.navigation.Router
import org.project.we3.app.navigation.Screen

class FirestoreDBRepositoryImpl(
    private val firestore: FirebaseFirestore,
): FirestoreDBRepository {
    override suspend fun fetchCamera(): List<Camera> {
        var cameras = emptyList<Camera>()
        try {
            val snapshot = firestore.collection("CameraList").get().await()  // 🔥 Await the result

            cameras = snapshot.documents.map { document ->
                Log.d("ViewAllCamera", "${document.id} => ${document.data}")
                Camera(
                    firestoreId = document.id,  // 🔥 Store actual Firestore ID
                    name = document.getString("name") ?: "",
                    unitPrice = document.getDouble("unitPrice") ?: 0.0,
                    detail = document.getString("detail") ?: "",
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

    override suspend fun addCamera(camera: Camera, context: Context, cameraViewModel: CameraViewModel) {
        // Add camera to Firestore
        firestore.collection("CameraList")
            .add(camera)
            .addOnSuccessListener {
                Toast.makeText(context, "Camera added successfully!", Toast.LENGTH_SHORT).show()
                cameraViewModel.refreshCameras()
                Router.navigateTo(Screen.ViewAllCameraScreen)
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error adding camera", Toast.LENGTH_SHORT).show()
            }

    }

    override suspend fun updateCamera(camera: Camera, context: Context, id: String, cameraViewModel: CameraViewModel) {
        val cameraData = hashMapOf(
            "name" to camera.name,
            "details" to camera.detail,
            "mrp" to camera.mrp,
            "unitPrice" to camera.unitPrice,
            "gst" to camera.gst,
            "quantity" to camera.quantity
        )
        // Update camera in Firestore
        firestore.collection("CameraList")
            .document(id)
            .set(cameraData)
            .addOnSuccessListener {
                Router.navigateTo(Screen.ViewAllCameraScreen)
                cameraViewModel.refreshCameras()
                Toast.makeText(context, "Camera updated successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error updating camera", Toast.LENGTH_SHORT).show()
            }
    }

    override suspend fun deleteCamera(camera: Camera, context: Context, cameraViewModel: CameraViewModel) {
        // Delete camera from Firestore
        firestore.collection("CameraList").document(camera.firestoreId).delete()
            .addOnSuccessListener {
                Router.navigateTo(Screen.ViewAllCameraScreen)
                cameraViewModel.refreshCameras()
                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Delete Failed", Toast.LENGTH_SHORT).show()
            }
    }
}