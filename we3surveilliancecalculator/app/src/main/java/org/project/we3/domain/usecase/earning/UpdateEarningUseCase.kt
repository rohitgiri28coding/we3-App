package org.project.we3.domain.usecase.earning

import android.util.Log
import org.project.we3.data.model.Camera
import org.project.we3.data.model.Quotation
import org.project.we3.data.repository.EarningRepository
import org.project.we3.domain.usecase.CalculateTotalPriceUseCase
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class UpdateEarningUseCase(
    private val earningRepository: EarningRepository,
    private val calculateTotalPriceUseCase: CalculateTotalPriceUseCase
) {
    suspend operator fun invoke(quotation: Quotation, cameras: List<Camera>) {
        var totalAmount = 0.0
        if (quotation.isActive) {
            val cameras = cameras.filter { camera ->
                quotation.camera.any { it.firestoreId == camera.firestoreId }
            }
             totalAmount = calculateTotalPriceUseCase.invoke(
                cameras,
                quotation.quantity,
                quotation.discountAmount
            )
            quotation.completionDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        }else{
            totalAmount = quotation.prepaymentAmount
        }
        Log.d("UpdateEarningUseCase", "Total Amount: $quotation")
        if (quotation.prepaymentAmount > 0) {
            earningRepository.updateTotalEarning(quotation.firestoreId, totalAmount, quotation.completionDate)
        }else{
            earningRepository.addEarning(quotation)
        }
    }
}