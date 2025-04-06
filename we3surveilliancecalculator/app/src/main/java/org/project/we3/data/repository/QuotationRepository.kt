package org.project.we3.data.repository

import org.project.we3.data.model.Quotation

interface QuotationRepository {
    suspend fun addQuotation(quotation: Quotation)
    suspend fun fetchQuotations(isActive: Boolean): List<Quotation>
    suspend fun updateQuotation(updatedQuotation: Quotation)
    suspend fun removeQuotation(quotationId: String)
    suspend fun toggleActiveStatus(quotationId: String, updatedStatus: Boolean)
    suspend fun fetchSpecificQuotation(quotationId: String): Quotation
}