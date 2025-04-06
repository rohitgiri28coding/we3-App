package org.project.we3.domain.usecase.earning

import org.project.we3.data.model.Quotation
import org.project.we3.data.repository.EarningRepository

class AddEarningUseCase(
    private val earningRepository: EarningRepository,
) {
    suspend operator fun invoke(quotation: Quotation) {
        earningRepository.addEarning(quotation)
    }
}