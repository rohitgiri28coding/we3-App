package org.project.we3.domain.usecase.quotation

import org.project.we3.data.model.Quotation
import org.project.we3.data.repository.QuotationRepository

class FetchQuotationUseCase(
    private val quotationRepository: QuotationRepository,
    private val sortAndFilterQuotationUseCase: SortAndFilterQuotationUseCase
) {
    suspend operator fun invoke(isActive: Boolean): List<Quotation> {
        val quotation = quotationRepository.fetchQuotations(isActive)
        return sortAndFilterQuotationUseCase.invoke(isActive, quotation)
    }
}