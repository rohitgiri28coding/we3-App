package org.project.we3.data.datsource

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.project.we3.data.model.Camera
import org.project.we3.data.model.Quotation
import org.project.we3.data.repository.QuotationRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class QuotationRepositoryImpl(private val firestore: FirebaseFirestore): QuotationRepository {
    override suspend fun addQuotation(quotation: Quotation) {
        val quotationMap = hashMapOf(
            "id" to quotation.id,
            "customerName" to quotation.customerName,
            "phoneNumber" to quotation.phoneNumber,
            "prepaymentAmount" to quotation.prepaymentAmount,
            "extraDiscount" to quotation.extraDiscount,
            "discountAmount" to quotation.discountAmount,
            "priority" to quotation.priority,
            "dateGenerated" to quotation.dateGenerated,
            "expiryDate" to quotation.expiryDate,
            "cameraName" to quotation.camera.map { it.name }, // Corrected to map to a list of names
            "cameraId" to quotation.camera.map { it.firestoreId },   // Corrected to map to a list of ids
            "quantity" to quotation.quantity,
            "isActive" to quotation.isActive,
            "completionDate" to quotation.completionDate
        )
        try {
            val doc = firestore.collection("quotations").add(quotationMap).await()
            quotation.firestoreId = doc.id
        } catch (e: Exception) {
            Log.e("Firestore", "Error adding quotation: ${e.message}")
        }

    }

    override suspend fun fetchQuotations(isActive: Boolean): List<Quotation> {
        try {
            val snapshot = firestore.collection("quotations").whereEqualTo("isActive", isActive) .get().await()

            val quotations = snapshot.documents.mapNotNull { doc ->
                val cameraNames = (doc.get("cameraName") as? List<*>)?.map { it.toString() } ?: emptyList()

                // Convert camera IDs to strings safely
                val cameraIds = (doc.get("cameraId") as? List<*>)?.map { it.toString() } ?: emptyList()

                val quantityList = (doc.get("quantity") as? List<*>)?.mapNotNull { (it as? Number)?.toInt() } ?: emptyList()

                val cameraList = cameraNames.zip(cameraIds).map { (name, id) ->
                    Camera(firestoreId = id, name = name)
                }

                Quotation(
                    id = doc.getString("id") ?: "",
                    firestoreId = doc.id,
                    customerName = doc.getString("customerName") ?: "",
                    dateGenerated = doc.getString("dateGenerated") ?: "",
                    priority = doc.getBoolean("priority") == true,
                    phoneNumber = doc.getString("phoneNumber") ?: "",
                    camera = cameraList,
                    quantity = quantityList,
                    expiryDate = doc.getString("expiryDate") ?: "",
                    prepaymentAmount = doc.getDouble("prepaymentAmount") ?: 0.0,
                    discountAmount = doc.getDouble("discountAmount") ?: 0.0,
                    isActive = doc.getBoolean("isActive") == true,
                    extraDiscount = doc.getBoolean("extraDiscount") == true,
                    completionDate = doc.getString("completionDate") ?: ""
                )
            }
            return quotations
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error fetching active quotations", e)
        }
        return emptyList()
    }


    override suspend fun updateQuotation(
        quotation: Quotation
    ) {
        try {
            firestore.collection("quotations").document(quotation.firestoreId).update(
                mapOf(
                    "customerName" to quotation.customerName,
                    "phoneNumber" to quotation.phoneNumber,
                    "prepaymentAmount" to quotation.prepaymentAmount,
                    "extraDiscount" to quotation.extraDiscount,
                    "discountAmount" to quotation.discountAmount,
                    "priority" to quotation.priority,
                    "dateGenerated" to quotation.dateGenerated,
                    "expiryDate" to quotation.expiryDate,
                    "cameraName" to quotation.camera.map { it.name }, // Corrected to map to a list of names
                    "cameraId" to quotation.camera.map { it.firestoreId },   // Corrected to map to a list of ids
                    "quantity" to quotation.quantity,
                )
            ).await()

        }catch (e: Exception){
            Log.e("QuotationDetailViewModel", "Error updating quotation", e)
        }
    }

    override suspend fun removeQuotation(quotationId: String) {
        try {
            firestore.collection("quotations").document(quotationId).delete().await()
        } catch (e: Exception) {
            Log.e("QuotationDetailViewModel", "Error deleting quotation", e)
        }
    }

    override suspend fun toggleActiveStatus(quotationId: String, updatedStatus: Boolean) {
        val updatedFields = mapOf(
            "isActive" to updatedStatus,
            "completionDate" to if (updatedStatus) "" else LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        )
        firestore.collection("quotations").document(quotationId).update(updatedFields).await()
    }

    override suspend fun fetchSpecificQuotation(quotationId: String): Quotation {

        val doc = firestore.collection("quotations").document(quotationId).get().await()
        val cameraNames = (doc.get("cameraName") as? List<*>)?.map { it.toString() } ?: emptyList()

        // Convert camera IDs to strings safely
        val cameraIds =
            (doc.get("cameraId") as? List<*>)?.map { it.toString() } ?: emptyList()

        val quantityList =
            (doc.get("quantity") as? List<*>)?.mapNotNull { (it as? Number)?.toInt() }
                ?: emptyList()

        val cameraList = cameraNames.zip(cameraIds).map { (name, id) ->
            Camera(firestoreId = id, name = name)
        }

        val quotation = Quotation(
            id = doc.getString("id") ?: "",
            firestoreId = doc.id,
            dateGenerated = doc.getString("dateGenerated") ?: "",
            expiryDate = doc.getString("expiryDate") ?: "",
            customerName = doc.getString("customerName") ?: "",
            phoneNumber = doc.getString("phoneNumber") ?: "",
            camera = cameraList,
            quantity = quantityList,
        )

        return quotation

    }
}