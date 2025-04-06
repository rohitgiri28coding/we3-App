package org.project.we3.domain.usecase.quotation

import org.project.we3.data.model.Quotation
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class SortAndFilterQuotationUseCase {

    operator fun invoke(isActive: Boolean, quotations: List<Quotation>): List<Quotation>{
        return if (isActive){
            sortQuotationByPriorityThenDate(quotations)
        }else{
            sortQuotationsByCompletionDate(quotations)
        }
    }

    private fun sortQuotationsByCompletionDate(quotations: List<Quotation>): List<Quotation> {
        val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return quotations.sortedWith { q1, q2 ->
            try {
                val date1 = LocalDate.parse(q1.completionDate, dateFormatter)
                val date2 = LocalDate.parse(q2.completionDate, dateFormatter)
                date2.compareTo(date1) // Newest first
            } catch (_: DateTimeParseException) {
                if (q1.completionDate.isEmpty() && q2.completionDate.isNotEmpty()) return@sortedWith 1
                if (q1.completionDate.isNotEmpty() && q2.completionDate.isEmpty()) return@sortedWith -1
                if (q1.completionDate.isEmpty() && q2.completionDate.isEmpty()) return@sortedWith 0
                return@sortedWith 0
            }
        }
    }

    private fun sortQuotationByPriorityThenDate(quotations: List<Quotation>): List<Quotation>{
        val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return quotations.filter {it.isActive}.sortedWith(compareByDescending<Quotation> { it.priority }
            .thenComparator { q1, q2 ->
                try {
                    val date1 = LocalDate.parse(q1.dateGenerated, dateFormatter)
                    val date2 = LocalDate.parse(q2.dateGenerated, dateFormatter)
                    date2.compareTo(date1) // Newest first
                } catch (_: DateTimeParseException) {
                    if (q1.dateGenerated.isEmpty() && q2.dateGenerated.isNotEmpty()) return@thenComparator 1
                    if (q1.dateGenerated.isNotEmpty() && q2.dateGenerated.isEmpty()) return@thenComparator -1
                    if (q1.dateGenerated.isEmpty() && q2.dateGenerated.isEmpty()) return@thenComparator 0
                    return@thenComparator 0
                }
            })
    }

}