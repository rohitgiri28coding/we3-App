package org.project.we3.ui.screens

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.project.we3.app.createQuotationPDF
import org.project.we3.app.db.Quotation

class CustomerDetailViewModel: ViewModel() {
    private val _currentQuotation = MutableStateFlow(Quotation(emptyList(), emptyList()))

    var customerName by mutableStateOf("")
        private set // Make it private set to enforce update functions

    var phoneNumber by mutableStateOf("")
        private set

    var prepayment by mutableStateOf("0")
        private set

    var discountAmount by mutableStateOf("0")
        private set

    var extraDiscount by mutableStateOf(false)
        private set

    var priority by mutableStateOf(false)
        private set

    var showDatePicker by mutableStateOf(false)

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

    fun setQuotation(quotation: Quotation) {
        viewModelScope.launch {
            _currentQuotation.emit(quotation)
        }
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    fun generateAndSaveQuotation(context: Context) {
        if (customerName.isEmpty()){
            Toast.makeText(context, "Please enter customer name", Toast.LENGTH_SHORT).show()
        }else if (phoneNumber.isEmpty() || phoneNumber.length != 10){
            Toast.makeText(context, "Please enter valid phone number", Toast.LENGTH_SHORT).show()
        }else if(extraDiscount && discountAmount.isEmpty()){
            Toast.makeText(context, "Please enter discount amount or uncheck extra discount", Toast.LENGTH_SHORT).show()
        }
        else{
            _currentQuotation.value = _currentQuotation.value.copy(
                customerName = customerName,
                phoneNumber = phoneNumber,
                prepaymentAmount = prepayment.toDouble(),
                extraDiscount = extraDiscount,
                discountAmount = discountAmount.toDouble(),
                priority = priority
            )
            createQuotationPDF(
                context,
                _currentQuotation.value
            )
        }
    }
    fun updateQuotationDateRange(startDate: Long?, endDate: Long?) {
        _currentQuotation.value = _currentQuotation.value.copy(dateGenerated = startDate.toString(), validity = endDate.toString())
    }
}