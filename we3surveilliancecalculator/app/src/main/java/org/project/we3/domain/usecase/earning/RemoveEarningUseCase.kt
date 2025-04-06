package org.project.we3.domain.usecase.earning

import org.project.we3.data.repository.EarningRepository

class RemoveEarningUseCase (
    private val earningRepository: EarningRepository
){
    suspend operator fun invoke(quotationId: String){
        earningRepository.deleteEarning(quotationId, null)

    }
}