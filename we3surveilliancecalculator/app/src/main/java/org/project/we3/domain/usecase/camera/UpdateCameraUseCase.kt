package org.project.we3.domain.usecase.camera

import org.project.we3.data.model.Camera
import org.project.we3.data.repository.CameraRepository

class UpdateCameraUseCase (
    private val cameraRepository: CameraRepository
){
    suspend operator fun invoke(camera: Camera){
        cameraRepository.updateCamera(camera)
    }

}