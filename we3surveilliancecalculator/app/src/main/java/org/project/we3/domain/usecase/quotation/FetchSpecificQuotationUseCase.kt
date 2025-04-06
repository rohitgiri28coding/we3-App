package org.project.we3.domain.usecase.quotation

import org.project.we3.data.model.Quotation
import org.project.we3.data.repository.QuotationRepository

class FetchSpecificQuotationUseCase(
    private val quotationRepository: QuotationRepository
) {
    suspend operator fun invoke(quotationId: String): Quotation {
        return quotationRepository.fetchSpecificQuotation(quotationId)
    }
}