package org.project.we3.domain.usecase.camera

import org.project.we3.data.repository.CameraRepository

class RemoveCameraUseCase(
    private val cameraRepository: CameraRepository
) {
    suspend operator fun invoke(cameraId: String) {
        cameraRepository.deleteCamera(cameraId)
    }
}