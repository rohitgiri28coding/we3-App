package org.project.we3.domain.usecase.earning

import org.project.we3.data.model.Earning
import org.project.we3.data.repository.EarningRepository

class FetchEarningUseCase(
    private val earningRepository: EarningRepository,
    private val sortEarningByDateUseCase: SortEarningByDateUseCase
) {
    suspend operator fun invoke(): List<Earning> {
        return sortEarningByDateUseCase(earningRepository.fetchEarnings())
    }
}