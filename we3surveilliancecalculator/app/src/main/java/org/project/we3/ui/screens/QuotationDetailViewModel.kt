package org.project.we3.ui.screens

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.project.we3.app.Camera
import org.project.we3.app.Earning
import org.project.we3.app.db.Quotation
import org.project.we3.app.navigation.Router
import org.project.we3.app.navigation.Screen
import org.project.we3.app.openPdfFile
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class QuotationDetailViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val _quotation = MutableStateFlow<Quotation>(Quotation())
    val quotation: StateFlow<Quotation?> = _quotation
    var cameraList: StateFlow<List<Camera>> = MutableStateFlow<List<Camera>>(emptyList())
    var showDeleteDialog by mutableStateOf(false)  // State to show/hide dialog
    var showCompletedDialog by mutableStateOf(false) // State to show/hide dialog
    var showLoader by  mutableStateOf(false) // State to show/hide loader

    fun setQuotationAndCamera(quotation: Quotation, camera: StateFlow<List<Camera>>) {
        viewModelScope.launch {
            _quotation.emit(quotation)
            cameraList = camera
            calculateTotalAmount(_quotation.value)
        }
    }

    fun navigateToEditScreen(quotation: Quotation) {
        Router.navigateTo(Screen.QuotationEditScreen(quotation))
    }

    fun deleteQuotation(quotation: Quotation) {
        viewModelScope.launch(Dispatchers.IO) {
            showLoader = true // Move this to the start
            try {
                db.collection("quotations").document(quotation.firestoreId).delete().await()
                val doc =
                    db.collection("earnings").whereEqualTo("quotationId", quotation.firestoreId)
                        .get()
                        .await()

                withContext(Dispatchers.Main) {
                    doc.forEach {
                        db.collection("earnings").document(it.id).delete().await()
                    }
                }
                Router.navigateTo(Screen.AdminSectionNavigatorScreen)
            } catch (e: Exception) {
                Log.e("QuotationDetailViewModel", "Error deleting quotation", e)
            } finally {
                showLoader = false
            }
        }
    }

    fun markAsCompleted(quotation: Quotation) {
        try {
            quotation.completionDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            val updatedFields = mapOf(
                "isActive" to false,
                "completionDate" to quotation.completionDate
            )

            db.collection("quotations").document(quotation.firestoreId)
                .update(updatedFields)
                .addOnSuccessListener {
                    Log.d("QuotationDetailViewModel", "Quotation marked as completed")
                    Router.navigateTo(Screen.AdminSectionNavigatorScreen)
                }
                .addOnFailureListener { exception ->
                    Log.e("QuotationDetailViewModel", "Error updating quotation", exception)
                }
            addInEarnings(quotation)
        }catch (e: Exception){
            e.printStackTrace()
        }finally {
            showLoader = false
        }

    }
    fun addInEarnings(quotation: Quotation){
        val totalAmount = calculateTotalAmount(quotation)
        viewModelScope.launch(Dispatchers.IO) {
            val doc =
                db.collection("earnings").whereEqualTo("quotationId", quotation.firestoreId)
                    .get()
                    .await()

            withContext(Dispatchers.Main) {
                if (doc.documents.isEmpty()) {
                    val earning = Earning(
                        date = quotation.completionDate,
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
                    db.collection("earnings").add(earnings).await()
                } else {
                    doc.forEach {
                        db.collection("earnings").document(it.id).update(
                            mapOf(
                                "totalEarning" to totalAmount,
                            )
                        )
                    }
                }
            }
        }
    }

    fun markAsActive(quotation: Quotation){
        try {
            val updatedFields = mapOf(
                "isActive" to true,
                "completionDate" to ""
            )

            db.collection("quotations").document(quotation.firestoreId)
                .update(updatedFields)
                .addOnSuccessListener {
                    Log.d("QuotationDetailViewModel", "Quotation marked as completed")
                    Router.navigateTo(Screen.AdminSectionNavigatorScreen)
                }
                .addOnFailureListener { exception ->
                    Log.e("QuotationDetailViewModel", "Error updating quotation", exception)
                }
            viewModelScope.launch(Dispatchers.IO) {
                val doc = db.collection("earnings").whereEqualTo("quotationId", quotation.firestoreId).get()
                    .await()
                withContext(Dispatchers.Main) {
                    doc.forEach {
                        db.collection("earnings").document(it.id).update(
                            mapOf(
                                "totalEarning" to it.data["prepayment"],
                            )
                        )
                    }
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }finally {
            showLoader = false
        }

    }

    private fun calculateTotalAmount(quotation: Quotation): Double {
        Log.d("QuotationDetailViewModel", "Calculating total amount for cameras: ${cameraList.value}")
        Log.d("QuotationDetailViewModel", "Quotation: ${quotation.camera}")
        val cameras = cameraList.value.filter { camera ->
            quotation.camera.any { it.firestoreId == camera.firestoreId }
        }

        Log.d("QuotationDetailViewModel", "Cameras: $cameras")
        var totalAmount = 0.0
        cameras.forEachIndexed { index, camera->
            val price = camera.unitPrice * quotation.quantity[index]
            totalAmount += (price + (price * (camera.gst / 100)))
        }
        totalAmount -= quotation.discountAmount
        Log.d("QuotationDetailViewModel", "Total amount: $totalAmount")
        return totalAmount
    }

    fun showPDF(context: Context, quotation: Quotation) {
        val directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
        val file = File(directoryPath, "${quotation.id} invoice.pdf")
        try {
            openPdfFile(context, file)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}