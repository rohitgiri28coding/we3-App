package org.project.we3.app.repository

import android.content.Context
import org.project.we3.app.Camera
import org.project.we3.app.db.CameraViewModel

interface FirestoreDBRepository {

    suspend fun fetchCamera(): List<Camera>

    suspend fun addCamera(camera: Camera, context: Context, cameraViewModel: CameraViewModel)

    suspend fun updateCamera(camera: Camera, context: Context, id: String, cameraViewModel: CameraViewModel)

    suspend fun deleteCamera(camera: Camera, context: Context, cameraViewModel: CameraViewModel)


}