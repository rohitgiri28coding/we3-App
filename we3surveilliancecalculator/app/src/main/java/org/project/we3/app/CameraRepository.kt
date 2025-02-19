package org.project.we3.app

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class CameraRepository(private val cameraDao: CameraDao) {

    val cameras: Flow<List<Camera>> = cameraDao.getAllCameras()

    suspend fun refreshCameras() {
        val db = FirebaseFirestore.getInstance()
        val snapshot = db.collection("CameraList").get().await()
        val cameraList = snapshot.toObjects(Camera::class.java)

        cameraDao.clearAll()  // Remove old data
        cameraDao.insertAll(cameraList)  // Insert fresh data
    }
}

