package org.project.we3.data.repository

import org.project.we3.data.model.Earning
import org.project.we3.data.model.Quotation

interface EarningRepository {
    suspend fun fetchEarnings(): List<Earning>
    suspend fun addEarning(quotation: Quotation)
    suspend fun updateTotalEarning(quotationId: String, totalEarning: Double, date: String)
    suspend fun updateEarning(quotation: Quotation, totalAmount: Double)
    suspend fun deleteEarning(quotationId: String?, earningId: String?)
}