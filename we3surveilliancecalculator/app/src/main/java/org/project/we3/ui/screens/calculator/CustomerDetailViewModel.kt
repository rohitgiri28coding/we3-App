package org.project.we3.ui.screens.calculator

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.project.we3.app.createQuotationPDF
import org.project.we3.data.model.Quotation
import org.project.we3.domain.usecase.camera.UpdateCameraQuantityUseCase
import org.project.we3.domain.usecase.earning.AddEarningUseCase
import org.project.we3.domain.usecase.quotation.AddQuotationUseCase
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CustomerDetailViewModel @Inject constructor(
    private val updateCameraQuantityUseCase: UpdateCameraQuantityUseCase,
    private val addQuotationUseCase: AddQuotationUseCase,
    private val addEarningUseCase: AddEarningUseCase
): ViewModel() {
    private val _currentQuotation = MutableStateFlow(Quotation())

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


    var currentMillis = currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
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
    fun updateQuotationDateRange(startDate: String?, endDate: String?) {
        if (isValidDate(startDate) && isValidDate(endDate)) {
            _currentQuotation.value = _currentQuotation.value.copy(
                dateGenerated = startDate.toString(),
                expiryDate = endDate.toString()
            )
        }else if(isValidDate(startDate)){
            _currentQuotation.value = _currentQuotation.value.copy(
                dateGenerated = startDate.toString(),
                expiryDate = convertDateToString(currentDate.plus(30, ChronoUnit.DAYS).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()).toString()
            )
        }else{
            _currentQuotation.value = _currentQuotation.value.copy(
                dateGenerated = convertDateToString(currentMillis)!!,
                expiryDate = convertDateToString(thirtyDaysLaterMillis)!!
            )
        }
    }
    fun convertDateToString(startDate: Long?): String?{
        return startDate?.let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        }

    }
    fun generateAndSaveQuotation(context: Context) {
        if(_currentQuotation.value.dateGenerated.isEmpty()){
            updateQuotationDateRange(null, null)
        }
        if (customerName.isEmpty()){
            Toast.makeText(context, "Please enter customer name", Toast.LENGTH_SHORT).show()
        }else if (phoneNumber.isEmpty() || phoneNumber.length != 10){
            Toast.makeText(context, "Please enter valid phone number", Toast.LENGTH_SHORT).show()
        }else if(extraDiscount && discountAmount.isEmpty() || discountAmount == "0"){
            Toast.makeText(context, "Please enter discount amount or uncheck extra discount", Toast.LENGTH_SHORT).show()
        }
        else{
            _currentQuotation.value = _currentQuotation.value.copy(
                id = UUID.randomUUID().toString(),
                customerName = customerName,
                phoneNumber = phoneNumber,
                prepaymentAmount = prepayment.toDoubleOrNull() ?: 0.0,
                extraDiscount = extraDiscount,
                discountAmount = discountAmount.toDoubleOrNull() ?: 0.0,
                priority = priority
            )
            viewModelScope.launch(Dispatchers.IO) {
                createQuotationPDF(context, _currentQuotation.value)
                addQuotationUseCase.invoke(_currentQuotation.value)
                withContext(Dispatchers.Main) {
                    updateStock(_currentQuotation.value)
                    addEarningUseCase.invoke(_currentQuotation.value)
                }
                resetQuotation()
            }
        }
    }
    private fun resetQuotation(){
        customerName = ""
        phoneNumber = ""
        prepayment = ""
        discountAmount = ""
        extraDiscount = false
        priority = false
        showDatePicker = false
        _currentQuotation.value = Quotation()
    }


    private fun updateStock(quotation: Quotation){
        quotation.camera.forEachIndexed { index, camera ->
            val quantity = quotation.quantity[index]
            val newQuantity = camera.quantity - quantity
            viewModelScope.launch(Dispatchers.IO) {
                updateCameraQuantityUseCase.invoke(camera.firestoreId, newQuantity)
            }
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
