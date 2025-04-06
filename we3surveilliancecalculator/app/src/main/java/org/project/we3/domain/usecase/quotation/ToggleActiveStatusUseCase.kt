package org.project.we3.domain.usecase.quotation

import org.project.we3.data.repository.QuotationRepository

class ToggleActiveStatusUseCase(
    private val quotationRepository: QuotationRepository,
) {
    suspend operator fun invoke(quotationId: String, updatedStatus: Boolean){
        quotationRepository.toggleActiveStatus(quotationId, updatedStatus)
    }
}