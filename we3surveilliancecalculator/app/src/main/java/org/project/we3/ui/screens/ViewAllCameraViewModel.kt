package org.project.we3.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.project.we3.app.Camera
import org.project.we3.app.navigation.Router
import org.project.we3.app.navigation.Screen

class ViewAllCameraViewModel: ViewModel() {
    var isLoading by  mutableStateOf(true)
    private val db = Firebase.firestore
    var cameras by mutableStateOf<List<Camera>>(emptyList())

    init {
        viewModelScope.launch {
            fetchCameras()
        }
    }

    private suspend fun fetchCameras() {
        isLoading = true
        try {
            val snapshot = db.collection("CameraList").get().await()  // 🔥 Await the result

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
        finally {
            isLoading = false
        }
    }
    fun selectCameraAndNavigate(camera: Camera) {
        Router.navigateTo(Screen.CameraDetailScreen(camera))
    }

    fun navigateToAddCameraScreen() {
        Router.navigateTo(Screen.AddNewCameraScreen)
    }
}