package org.project.we3.domain.usecase.quotation

import org.project.we3.data.model.Quotation
import org.project.we3.data.repository.QuotationRepository

class AddQuotationUseCase(
    private val quotationRepository: QuotationRepository
) {
    suspend operator fun invoke(quotation: Quotation) {
        quotationRepository.addQuotation(quotation)
    }
}