package org.project.we3.app.db

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.project.we3.app.Camera

class CameraViewModel(application: Application) : AndroidViewModel(application) {

    private val database = CameraDatabase.getDatabase(application)
    private val repository = CameraRepository(database.cameraDao())

    init {
        fetchAndUpdateDB()  // Fetch and save Firestore data in Room
    }
    val cameras: StateFlow<List<Camera>> = repository.cameras.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptyList()
    )


    private fun fetchAndUpdateDB() {
        viewModelScope.launch {
            repository.refreshCameras(fetchCameras())
        }
    }
    suspend fun fetchCameras(): List<Camera> {
        val db = FirebaseFirestore.getInstance()
        var cameraList = emptyList<Camera>()
        try {
            val snapshot = db.collection("CameraList").get().await()  // 🔥 Await the result

            cameraList = snapshot.documents.map { document ->
                Log.d("CameraRepository", "${document.id} => ${document.data}")
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
        return cameraList
    }
}
