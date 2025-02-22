package org.project.we3.ui.screens

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.project.we3.app.Camera
import org.project.we3.app.createQuotationPDF

class SurveillanceCalculatorViewModel : ViewModel() {
    var selectedCamera = mutableStateOf<Camera?>(null)
    var quantity = mutableStateOf("")

    fun selectCamera(camera: Camera) {
        selectedCamera.value = camera
    }

    fun updateQuantity(newQuantity: String) {
        quantity.value = newQuantity
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun validateAndGenerateQuotation(context: Context) {
        val selectedCam = selectedCamera.value
        val qty = quantity.value.toIntOrNull()

        when {
            selectedCam == null -> {
                Toast.makeText(context, "Please select a camera", Toast.LENGTH_SHORT).show()
            }
            qty == null || qty <= 0 -> {
                Toast.makeText(context, "Enter a valid quantity", Toast.LENGTH_SHORT).show()
            }
            qty > selectedCam.quantity -> {
                Toast.makeText(context, "Only ${selectedCam.quantity} cameras available", Toast.LENGTH_SHORT).show()
                quantity.value = selectedCam.quantity.toString()
            }
            else -> {
                val totalPrice = selectedCam.unitPrice * qty
                Toast.makeText(context, "Quotation: ₹$totalPrice for $qty", Toast.LENGTH_LONG).show()
                createQuotationPDF(context, listOf(selectedCam), qty)
            }
        }
    }
}
