package org.project.we3.ui.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.project.we3.app.Earning
import org.project.we3.app.createQuotationPDF
import org.project.we3.app.db.Quotation
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

class CustomerDetailViewModel: ViewModel() {
    private val db = Firebase.firestore
    private val _currentQuotation = MutableStateFlow(Quotation(camera = emptyList(), quantity = emptyList()))

    var customerName by mutableStateOf("")
        private set

    var phoneNumber by mutableStateOf("")
        private set

    var prepayment by mutableStateOf("")
        private set

    var discountAmount by mutableStateOf("")
        private set

    var extraDiscount by mutableStateOf(false)
        private set

    var priority by mutableStateOf(false)
        private set

    var showDatePicker by mutableStateOf(false)


    private val currentDate: LocalDate = LocalDate.now()
    private val thirtyDaysLater: LocalDate = currentDate.plus(30, ChronoUnit.DAYS)


    val currentMillis = currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    val thirtyDaysLaterMillis = thirtyDaysLater.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()


    fun updateCustomerName(name: String) {
        customerName = name
    }

    fun updatePhoneNumber(number: String) {
        phoneNumber = number
    }

    fun updatePrepayment(amount: String) {
        prepayment = amount
    }

    fun updateDiscountAmount(amount: String) {
        discountAmount = amount
    }

    fun updateExtraDiscount(value: Boolean) {
        extraDiscount = value
    }

    fun updatePriority(value: Boolean) {
        priority = value
    }

    fun updateQuotation(quotation: Quotation) {
        viewModelScope.launch {
            _currentQuotation.emit(quotation)
        }
    }
    fun updateQuotationDateRange(startDate: String?, endDate: String?, context: Context) {
        if (isValidDate(startDate) && isValidDate(endDate)) {
            _currentQuotation.value = _currentQuotation.value.copy(
                dateGenerated = startDate.toString(),
                expiryDate = endDate.toString()
            )
        }else{
            Toast.makeText(context, "Please select valid date range", Toast.LENGTH_SHORT).show()
        }
    }
    fun generateAndSaveQuotation(context: Context) {
        if (customerName.isEmpty()){
            Toast.makeText(context, "Please enter customer name", Toast.LENGTH_SHORT).show()
        }else if (phoneNumber.isEmpty() || phoneNumber.length != 10){
            Toast.makeText(context, "Please enter valid phone number", Toast.LENGTH_SHORT).show()
        }else if(extraDiscount && discountAmount.isEmpty() || discountAmount == "0"){
            Toast.makeText(context, "Please enter discount amount or uncheck extra discount", Toast.LENGTH_SHORT).show()
        }else if(_currentQuotation.value.dateGenerated.isEmpty()){
            Toast.makeText(context, "Please select a valid date range", Toast.LENGTH_SHORT).show()
        }
        else{
            _currentQuotation.value = _currentQuotation.value.copy(
                customerName = customerName,
                phoneNumber = phoneNumber,
                prepaymentAmount = prepayment.toDoubleOrNull() ?: 0.0,
                extraDiscount = extraDiscount,
                discountAmount = discountAmount.toDoubleOrNull() ?: 0.0,
                priority = priority
            )
            viewModelScope.launch(Dispatchers.IO) {
                addQuotationToFirestore(_currentQuotation.value)
                createQuotationPDF(context, _currentQuotation.value)
            }
        }
    }
    private fun addQuotationToFirestore(quotation: Quotation) {
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
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val doc = db.collection("quotations").add(quotationMap).await()
                quotation.firestoreId = doc.id

                if (quotation.prepaymentAmount > 0) {
                    withContext(Dispatchers.Main) {
                        addInEarning(quotation)
                    }
                }
            } catch (e: Exception) {
                Log.e("Firestore", "Error adding quotation: ${e.message}")
            }
        }
    }

    private fun addInEarning(quotation: Quotation) {

        val earning = Earning(
            prepayment = quotation.prepaymentAmount,
            quotationId = quotation.firestoreId,
            date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString(),
            pdfId = quotation.id,
            totalEarning = quotation.prepaymentAmount
        )
        val earningMap = hashMapOf(
            "id" to earning.id,
            "quotationId" to earning.quotationId,
            "prepayment" to earning.prepayment,
            "totalEarning" to earning.totalEarning,
            "date" to earning.date,
            "pdfId" to earning.pdfId
        )
        viewModelScope.launch(Dispatchers.IO) {
            db.collection("earnings").add(earningMap)

        }
    }

}
fun isValidDate(dateString: String?): Boolean {
    if (dateString == null) return false // Handle null case

    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    return try {
        LocalDate.parse(dateString, formatter)
        true // Parsing succeeded, so it's a valid date
    } catch (_: DateTimeParseException) {
        false // Parsing failed, so it's an invalid date
    }
}

fun isValidDateSet(dateString1: String?, dateString2: String): Boolean {
    if (dateString1 == null) return false // Handle null case

    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    return try {
        val date1 = LocalDate.parse(dateString1, formatter)
        val date2 = LocalDate.parse(dateString2, formatter)
        date1.isBefore(date2)
        true // Parsing succeeded, so it's a valid date
    } catch (_: DateTimeParseException) {
        false // Parsing failed, so it's an invalid date
    }
}
