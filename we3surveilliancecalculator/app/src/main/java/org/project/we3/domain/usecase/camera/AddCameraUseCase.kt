package org.project.we3.domain.usecase.camera

import org.project.we3.data.model.Camera
import org.project.we3.data.repository.CameraRepository

class AddCameraUseCase (
    private val cameraRepository: CameraRepository
){
    suspend operator fun invoke(camera: Camera) {
        cameraRepository.addCamera(camera)
    }
}