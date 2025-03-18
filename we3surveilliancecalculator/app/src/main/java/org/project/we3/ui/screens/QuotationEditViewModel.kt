package org.project.we3.ui.screens

import android.content.Context
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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.project.we3.app.Camera
import org.project.we3.app.createQuotationPDF
import org.project.we3.app.db.Quotation
import org.project.we3.app.navigation.Router
import org.project.we3.app.navigation.Screen
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class QuotationEditViewModel : ViewModel() {
    private val db = Firebase.firestore

    private val _uiState = MutableStateFlow(QuotationUiState())
    val uiState: StateFlow<QuotationUiState> = _uiState.asStateFlow()

    private val _showAddCameraDialog = MutableStateFlow(false)
    val showAddCameraDialog: StateFlow<Boolean> = _showAddCameraDialog.asStateFlow()

    private val _selectedCamera = MutableStateFlow<Camera?>(null)
    val selectedCamera: StateFlow<Camera?> = _selectedCamera.asStateFlow()

    private val _newCameraQuantity = MutableStateFlow("")
    val newCameraQuantity: StateFlow<String> = _newCameraQuantity.asStateFlow()

    var cameras = emptyList<Camera>()

    var showDatePicker by mutableStateOf(false)

    fun setQuotation(quotation: Quotation) {
        viewModelScope.launch {
            _uiState.emit(
                QuotationUiState(
                    quotation = quotation,
                    customerName = quotation.customerName,
                    phoneNumber = quotation.phoneNumber,
                    dateGenerated = quotation.dateGenerated,
                    expiryDate = quotation.expiryDate,
                    prepaymentAmount = quotation.prepaymentAmount.toString(),
                    discountAmount = quotation.discountAmount.toString(),
                    priority = quotation.priority,
                    isActive = quotation.isActive,
                    extraDiscount = quotation.extraDiscount
                )
            )
        }
    }

    fun updateSelectedCamera(index: Int, camera: Camera) {
        viewModelScope.launch {
            val updatedCameras = _uiState.value.quotation.camera.toMutableList().apply {
                this[index] = camera
            }
            _uiState.emit(_uiState.value.copy(quotation = _uiState.value.quotation.copy(camera = updatedCameras)))
        }
    }

    fun updateQuantity(index: Int, quantity: String) {
        val newQuantity = quantity.toIntOrNull() ?: 0
        viewModelScope.launch {
            val updatedQuantities = _uiState.value.quotation.quantity.toMutableList().apply {
                this[index] = newQuantity
            }
            _uiState.emit(_uiState.value.copy(quotation = _uiState.value.quotation.copy(quantity = updatedQuantities)))
        }
    }

    fun removeCamera(index: Int) {
        viewModelScope.launch {
            val updatedCameras = _uiState.value.quotation.camera.filterIndexed { i, _ -> i != index }
            val updatedQuantities = _uiState.value.quotation.quantity.filterIndexed { i, _ -> i != index }
            _uiState.emit(_uiState.value.copy(quotation = _uiState.value.quotation.copy(camera = updatedCameras, quantity = updatedQuantities)))
        }
    }

    fun fetchCameraFromDB(cameraList: List<Camera>) {
        cameras = cameraList
    }

    fun setShowAddCameraDialog(show: Boolean) {
        viewModelScope.launch { _showAddCameraDialog.emit(show) }
    }

    fun setSelectedCamera(camera: Camera?) {
        viewModelScope.launch { _selectedCamera.emit(camera) }
    }

    fun setNewCameraQuantity(quantity: String) {
        viewModelScope.launch { _newCameraQuantity.emit(quantity) }
    }

    fun addCamera(context: Context) {
        viewModelScope.launch {
            val selected = _selectedCamera.value
            val quantityToAdd = _newCameraQuantity.value.toIntOrNull()

            if (selected != null && quantityToAdd != null && quantityToAdd > 0) {
                val currentQuotation = _uiState.value.quotation

                val existingIndex = currentQuotation.camera.indexOfFirst { it.name == selected.name }

                if (existingIndex != -1) {
                    // Camera exists in the list, update quantity
                    val updatedQuantities = currentQuotation.quantity.toMutableList()
                    var newTotalQuantity = updatedQuantities[existingIndex] + quantityToAdd

                    // Ensure that total quantity does not exceed selectedCamera.quantity
                    if (newTotalQuantity <= selected.quantity) {
                        updatedQuantities[existingIndex] = newTotalQuantity
                        _uiState.emit(
                            _uiState.value.copy(
                                quotation = currentQuotation.copy(quantity = updatedQuantities)
                            )
                        )
                    } else {
                        newTotalQuantity = selected.quantity
                        updatedQuantities[existingIndex] = newTotalQuantity
                        _uiState.emit(
                            _uiState.value.copy(
                                quotation = currentQuotation.copy(quantity = updatedQuantities)
                            )
                        )
                        Toast.makeText(context, "Maximum available quantity ${selected.quantity}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Camera does not exist, add a new entry
                    val updatedCameras = currentQuotation.camera + selected
                    val updatedQuantities = currentQuotation.quantity + quantityToAdd

                    _uiState.emit(
                        _uiState.value.copy(
                            quotation = currentQuotation.copy(
                                camera = updatedCameras,
                                quantity = updatedQuantities
                            )
                        )
                    )
                }

                // Reset the selected camera and quantity
                _selectedCamera.emit(null)
                _newCameraQuantity.emit("")
                _showAddCameraDialog.emit(false)
            }
        }
    }


    fun updateCustomerName(name: String) { _uiState.value = _uiState.value.copy(customerName = name) }
    fun updatePhoneNumber(number: String) { _uiState.value = _uiState.value.copy(phoneNumber = number) }
    fun updateDateGenerated(date: String) { if(isValidDate(date))_uiState.value = _uiState.value.copy(dateGenerated = date) }
    fun updateExpiryDate(validity: String) { if (isValidDate(validity))_uiState.value = _uiState.value.copy(expiryDate = validity) }
    fun updatePrepaymentAmount(amount: String) {
        _uiState.value = _uiState.value.copy(prepaymentAmount = amount)
    }
    fun updateDiscountAmount(amount: String) {
        _uiState.value = _uiState.value.copy(discountAmount = amount)
        amount.toDoubleOrNull()?.let {
            if (it>0){
                _uiState.value = _uiState.value.copy(extraDiscount = true)
            }else{
                _uiState.value = _uiState.value.copy(extraDiscount = false)
            }
        }
    }
    fun updatePriority(priority: Boolean) { _uiState.value = _uiState.value.copy(priority = priority) }


    fun validateAndSave(context: Context, uiState: QuotationUiState) {
        when {
            uiState.customerName.isBlank() -> {
                Toast.makeText(context, "Enter a valid Customer Name", Toast.LENGTH_SHORT).show()
            }
            uiState.phoneNumber.length != 10 || !uiState.phoneNumber.all { it.isDigit() } -> {
                Toast.makeText(context, "Enter a valid 10-digit Phone Number", Toast.LENGTH_SHORT).show()
            }
            uiState.dateGenerated.isBlank() || !isValidDate(uiState.dateGenerated) -> {
                Toast.makeText(context, "Enter a valid Date Generated", Toast.LENGTH_SHORT).show()
            }
            uiState.expiryDate.isBlank() || !isValidDate(uiState.expiryDate) || !isValidDateSet(uiState.dateGenerated, uiState.expiryDate) -> {
                Toast.makeText(context, "Enter a Validity period", Toast.LENGTH_SHORT).show()
            }
            uiState.quotation.camera.isEmpty() -> {
                Toast.makeText(context, "Add at least one Camera", Toast.LENGTH_SHORT).show()
            }
            uiState.quotation.quantity.any { it <= 0 } -> {
                Toast.makeText(context, "Quantity must be greater than 0", Toast.LENGTH_SHORT).show()
            }
            else -> {
                updateQuotationFromUiState()
                updateQuotation(context, _uiState.value.quotation)
            }
        }
    }
    fun updateQuotationFromUiState() {
        viewModelScope.launch {
            val currentState = _uiState.value

            // Get full details of cameras in the quotation
            val updatedCameraList = currentState.quotation.camera.mapNotNull { camera ->
                cameras.find { it.firestoreId == camera.firestoreId }
            }

            _uiState.emit(
                currentState.copy(
                    quotation = currentState.quotation.copy(
                        customerName = currentState.customerName,
                        phoneNumber = currentState.phoneNumber,
                        dateGenerated = currentState.dateGenerated,
                        expiryDate = currentState.expiryDate,
                        prepaymentAmount = currentState.prepaymentAmount.toDoubleOrNull() ?: 0.0,
                        discountAmount = currentState.discountAmount.toDoubleOrNull() ?: 0.0,
                        priority = currentState.priority,
                        extraDiscount = currentState.extraDiscount,
                        isActive = currentState.isActive,
                        camera = updatedCameraList,
                    )
                )
            )
        }
    }

    private fun updateQuotation(context: Context, quotation: Quotation){
        db.collection("quotations").document(quotation.firestoreId).update(
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
        )
            .addOnSuccessListener {
                Toast.makeText(context, "Quotation Updated Successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error updating quotation", Toast.LENGTH_SHORT).show()
            }
        updateEarning(context, quotation)


    }
    private fun updateEarning(context: Context, quotation: Quotation) {
        viewModelScope.launch (Dispatchers.IO){
            val doc = db.collection("earnings")
                .whereEqualTo("quotationId", quotation.firestoreId).get().await()
            withContext(Dispatchers.Main) {
                doc.forEach {
                    db.collection("earnings").document(it.id).update(
                        if (!uiState.value.isActive)
                            mapOf(
                                "totalEarning" to calculateTotalAmount(quotation),
                                "prepayment" to quotation.prepaymentAmount
                            )
                        else
                            mapOf(
                                "totalEarning" to quotation.prepaymentAmount,
                                "prepayment" to quotation.prepaymentAmount
                            )
                    ).addOnSuccessListener {
                        Toast.makeText(context, "Earning Updated Successfully!", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(context, "Error updating earning", Toast.LENGTH_SHORT).show()
                    }
                }
                Router.navigateTo(Screen.AdminSectionNavigatorScreen)
            }
        }
        createQuotationPDF(context, quotation)
    }
    private fun calculateTotalAmount(quotation: Quotation): Double {
        return quotation.camera.zip(quotation.quantity) { camera, cameraQuantity ->
            val cameraPrice = camera.unitPrice
            val gstAmount = (cameraQuantity * cameraPrice * camera.gst / 100)
            (cameraQuantity * cameraPrice) + gstAmount
        }.sum() // Sum up the total earnings
    }

}

data class QuotationUiState(
    val quotation: Quotation = Quotation(camera = emptyList(), quantity = emptyList()),
    val customerName: String = "",
    val phoneNumber: String = "",
    val dateGenerated: String = "",
    val expiryDate: String = "",
    val prepaymentAmount: String = "",
    val discountAmount: String = "",
    val extraDiscount: Boolean = false,
    val priority: Boolean = false,
    val isActive: Boolean = false
)

fun parseDateToMillis(dateString: String): Long {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val localDate = LocalDate.parse(dateString, formatter)
    return localDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
}