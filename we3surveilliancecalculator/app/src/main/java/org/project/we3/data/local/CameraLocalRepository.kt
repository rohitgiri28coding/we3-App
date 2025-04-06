package org.project.we3.data.local

import android.util.Log
import kotlinx.coroutines.flow.Flow
import org.project.we3.data.model.Camera

class CameraLocalRepository(private val cameraDao: CameraDao) {

    val cameras: Flow<List<Camera>> = cameraDao.getAllCameras()

    suspend fun refreshCameras(cameraList: List<Camera>) {
        try {
            cameraDao.clearAll()  // Remove old data
            cameraDao.insertAll(cameraList)  // Insert fresh data

        } catch (e: Exception) {
            Log.e("CameraRepository", "Error fetching data from Firestore", e)
        }
    }
}



