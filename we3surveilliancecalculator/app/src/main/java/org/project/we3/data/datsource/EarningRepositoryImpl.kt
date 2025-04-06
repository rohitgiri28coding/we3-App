package org.project.we3.data.datsource

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.project.we3.data.model.Earning
import org.project.we3.data.model.Quotation
import org.project.we3.data.repository.EarningRepository

class EarningRepositoryImpl(private val firestore: FirebaseFirestore): EarningRepository {
    override suspend fun fetchEarnings(): List<Earning> {
        val doc =
            firestore.collection("earnings").get().await()
        val earnings = mutableListOf<Earning>()
        withContext(Dispatchers.Main) {
            doc.forEach {
                val earning = Earning(
                    firestoreId = it.id,
                    id = it.getString("id") ?: "",
                    date = it.getString("date") ?: "",
                    totalEarning = it.getDouble("totalEarning") ?: 0.0,
                    prepayment = it.getDouble("prepayment") ?: 0.0,
                    quotationId = it.getString("quotationId") ?: "",
                    pdfId = it.getString("pdfId") ?: ""
                )
                earnings.add(earning)
            }
        }
        return earnings
    }

    override suspend fun addEarning(quotation: Quotation) {
        val earning = Earning(
            date = quotation.dateGenerated,
            totalEarning = quotation.prepaymentAmount,
            prepayment = quotation.prepaymentAmount,
            quotationId = quotation.firestoreId,
            pdfId = quotation.id
        )
        val earnings = hashMapOf(
            "id" to earning.id,
            "date" to earning.date,
            "totalEarning" to earning.totalEarning,
            "prepayment" to earning.prepayment,
            "quotationId" to earning.quotationId,
            "pdfId" to earning.pdfId
        )
        firestore.collection("earnings").add(earnings).await()
    }

    override suspend fun updateTotalEarning(
        quotationId: String,
        totalEarning: Double,
        date: String
    ) {
        val doc =
            firestore.collection("earnings").whereEqualTo("quotationId", quotationId)
                .get()
                .await()
        withContext(Dispatchers.Main) {
            doc.forEach {
                firestore.collection("earnings").document(it.id)
                    .update("totalEarning", totalEarning, "date", date).await()
            }
        }
    }

    override suspend fun updateEarning(
        quotation: Quotation,
        totalAmount: Double
    ) {
        val doc =
            firestore.collection("earnings").whereEqualTo("quotationId", quotation.firestoreId)
                .get()
                .await()

        withContext(Dispatchers.Main) {
            if (doc.documents.isEmpty()) {
                val earning = Earning(
                    date = quotation.dateGenerated,
                    totalEarning = totalAmount,
                    prepayment = quotation.prepaymentAmount,
                    quotationId = quotation.firestoreId,
                    pdfId = quotation.id
                )
                val earnings = hashMapOf(
                    "id" to earning.id,
                    "date" to earning.date,
                    "totalEarning" to earning.totalEarning,
                    "prepayment" to earning.prepayment,
                    "quotationId" to earning.quotationId,
                    "pdfId" to earning.pdfId
                )
                firestore.collection("earnings").add(earnings).await()
            } else {
                doc.forEach {
                    firestore.collection("earnings").document(it.id).update(
                        mapOf(
                            "totalEarning" to totalAmount,
                            "date" to quotation.dateGenerated
                        )
                    )
                }
            }
        }
    }

    override suspend fun deleteEarning(
        quotationId: String?,
        earningId: String?
        ) {
        if (earningId.isNullOrBlank()) {
            val doc =
                firestore.collection("earnings").whereEqualTo("quotationId", quotationId)
                    .get()
                    .await()

            withContext(Dispatchers.Main) {
                doc.forEach {
                    firestore.collection("earnings").document(it.id).delete().await()
                }
            }
        }else{
            firestore.collection("earnings").document(earningId).delete().await()
        }
    }

}