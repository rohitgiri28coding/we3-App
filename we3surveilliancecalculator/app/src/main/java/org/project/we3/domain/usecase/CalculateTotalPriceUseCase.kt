package org.project.we3.domain.usecase

import org.project.we3.data.model.Camera

class CalculateTotalPriceUseCase {
    operator fun invoke(cameraList: List<Camera>, quantityList: List<Int>, totalDiscount: Double): Double {
        return cameraList.zip(quantityList) { camera, cameraQuantity ->
            val cameraPrice = camera.unitPrice
            val gstAmount = (cameraQuantity * cameraPrice * camera.gst / 100)
            (cameraQuantity * cameraPrice) + gstAmount
        }.sum() - totalDiscount
    }
}