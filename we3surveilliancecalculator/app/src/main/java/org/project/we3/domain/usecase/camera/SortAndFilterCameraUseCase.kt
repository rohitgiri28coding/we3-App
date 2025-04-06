package org.project.we3.domain.usecase.camera

import org.project.we3.data.model.Camera

class SortAndFilterCameraUseCase {
    fun filterAndSortLowStockCameras(cameras: List<Camera>, minimumQuantity: Int = 10): List<Camera> {
        return cameras.filter { it.quantity < minimumQuantity }.sortedBy { it.quantity }
    }
}