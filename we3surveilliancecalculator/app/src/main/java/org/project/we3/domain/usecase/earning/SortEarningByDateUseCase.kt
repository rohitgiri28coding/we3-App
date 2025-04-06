package org.project.we3.domain.usecase.earning

import org.project.we3.data.model.Earning
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SortEarningByDateUseCase {
    operator fun invoke(earnings: List<Earning>): List<Earning> {
        val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        return earnings.sortedWith(Comparator { e1, e2 ->
            val date1 = try { LocalDate.parse(e1.date, dateFormatter) } catch (_: Exception) { null }
            val date2 = try { LocalDate.parse(e2.date, dateFormatter) } catch (_: Exception) { null }

            // First compare by date (newest first)
            val dateComparison = when {
                date1 != null && date2 != null -> date2.compareTo(date1) // Newest first
                date1 != null -> -1 // Valid date comes before null
                date2 != null -> 1
                else -> 0
            }

            if (dateComparison != 0) {
                dateComparison
            } else {
                // Then compare by totalEarning descending
                e2.totalEarning.compareTo(e1.totalEarning)
            }
        })
    }
}
