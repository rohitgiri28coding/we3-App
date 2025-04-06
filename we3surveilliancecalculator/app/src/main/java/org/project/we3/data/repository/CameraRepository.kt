package org.project.we3.data.repository

import org.project.we3.data.model.Camera

interface CameraRepository {

    suspend fun fetchCamera(): List<Camera>

    suspend fun addCamera(camera: Camera)

    suspend fun updateCamera(camera: Camera)

    suspend fun deleteCamera(cameraId: String)

    suspend fun updateQuantity(cameraId: String, quantity: Int)

}