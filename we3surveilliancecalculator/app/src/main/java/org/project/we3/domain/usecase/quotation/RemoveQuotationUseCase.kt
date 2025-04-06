package org.project.we3.domain.usecase.quotation

import org.project.we3.data.model.Camera
import org.project.we3.data.model.Quotation
import org.project.we3.data.repository.CameraRepository
import org.project.we3.data.repository.QuotationRepository

class RemoveQuotationUseCase (
    private val quotationRepository: QuotationRepository,
    private val cameraRepository: CameraRepository
){
    suspend operator fun invoke(quotation: Quotation){
        val updatedCameras = retrieveCameraAndUpdateQuantity(quotation)
        updatedCameras.forEach { camera ->
            cameraRepository.updateQuantity(camera.firestoreId, camera.quantity)
        }
        quotationRepository.removeQuotation(quotation.firestoreId)
    }
    private suspend fun retrieveCameraAndUpdateQuantity(quotation: Quotation): List<Camera> {
        val updatedQuotation = quotationRepository.fetchSpecificQuotation(quotation.firestoreId)
        val allCameras = cameraRepository.fetchCamera()

        val updatedCameras = allCameras.map { camera ->
            val index = updatedQuotation.camera.indexOfFirst { it.firestoreId == camera.firestoreId }
            if (index != -1) {
                val extraQuantity = updatedQuotation.quantity.getOrNull(index) ?: 0
                camera.copy(quantity = camera.quantity + extraQuantity)
            } else {
                camera
            }
        }

        return updatedCameras.filter { updatedQuotation.camera.any { qCam -> qCam.firestoreId == it.firestoreId } }
    }

}