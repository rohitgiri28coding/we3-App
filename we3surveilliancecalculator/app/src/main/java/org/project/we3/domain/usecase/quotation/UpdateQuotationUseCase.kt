package org.project.we3.domain.usecase.quotation

import org.project.we3.data.model.Quotation
import org.project.we3.data.repository.QuotationRepository

class UpdateQuotationUseCase (
    private val quotationRepository: QuotationRepository
){
    suspend operator fun invoke(quotation: Quotation) {
        quotationRepository.updateQuotation(quotation)
    }
}