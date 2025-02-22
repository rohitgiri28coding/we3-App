package org.project.we3.ui.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import org.project.we3.app.Camera
import org.project.we3.app.navigation.Router
import org.project.we3.app.navigation.Screen

class CameraDetailsViewModel: ViewModel() {

    private val firestore = Firebase.firestore
    var isLoading by mutableStateOf(false)

    fun deleteCamera(camera: Camera, context: Context) {
        Log.d("Firestore", "Deleting camera: $camera")
        firestore.collection("CameraList").document(camera.firestoreId).delete()
            .addOnSuccessListener {
                isLoading = false
                Router.navigateTo(Screen.ViewAllCameraScreen)
                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                isLoading = false
                Toast.makeText(context, "Delete Failed", Toast.LENGTH_SHORT).show()
            }
    }

    fun navigateToEditScreen(camera: Camera) {
        Router.navigateTo(Screen.EditScreen(camera))
    }

}