package org.project.we3.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.project.we3.app.Camera
import org.project.we3.app.db.Quotation
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class FulfilledQuotationViewModel: ViewModel(){
    private val db = Firebase.firestore
    private val _fulfilledQuotations = MutableStateFlow<List<Quotation>>(emptyList())
    val fulfilledQuotations: StateFlow<List<Quotation>> = _fulfilledQuotations

    init {
        fetchFulfilledQuotations()
    }

    fun fetchFulfilledQuotations() {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("quotations")
                    .whereEqualTo("isActive", false) // Fetch only active quotations
                    .get()
                    .await()

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

                _fulfilledQuotations.emit(arrangePriorityBasis(quotations))
                Log.d("Firestore", "Fulfilled quotations fetched successfully with ${quotations.size} items")
            } catch (e: Exception) {
                Log.e("FirestoreError", "Error fetching active quotations", e)
            }
        }
    }

    fun arrangePriorityBasis(quotations: List<Quotation>): List<Quotation> {
        val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        return quotations.filter {!it.isActive}.sortedWith(compareByDescending<Quotation> { it.priority }
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