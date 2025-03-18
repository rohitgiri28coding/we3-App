package org.project.we3.ui.screens

import android.content.Context
import android.os.Environment
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
import org.project.we3.app.openPdfFile
import java.io.File

class EarningDetailViewModel: ViewModel() {
    private val db = Firebase.firestore
    private val _quotations = MutableStateFlow<Quotation>(Quotation())
    val quotations: StateFlow<Quotation> = _quotations

    fun fetchQuotation(quotationId: String) {
        viewModelScope.launch {
            try {
                val doc = db.collection("quotations").document(quotationId).get()
                    .await()
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
                    customerName = doc.getString("customerName") ?: "",
                    phoneNumber = doc.getString("phoneNumber") ?: "",
                    camera = cameraList,
                    quantity = quantityList,
                )
                Log.d("Firestore", "Converted Quotation: $quotation")
            _quotations.emit(quotation)
            } catch (e: Exception) {
                Log.e("FirestoreError", "Error fetching active quotations", e)
            }
        }
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