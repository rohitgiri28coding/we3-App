package org.project.we3.ui.screens

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.project.we3.app.Camera
import org.project.we3.app.db.Quotation
import org.project.we3.app.navigation.Router
import org.project.we3.app.navigation.Screen

class SurveillanceCalculatorViewModel: ViewModel() {
    var isLoading by mutableStateOf(true)
    var selectedCamera by mutableStateOf<Camera?>(null)
    var quantity by mutableStateOf("")

    var quotation by mutableStateOf(Quotation(emptyList<Camera>(), emptyList()))

    fun selectCamera(camera: Camera) {
        selectedCamera = camera
    }

    fun updateQuantity(newQuantity: String) {
        Log.d("Check",quantity)
        quantity = newQuantity
        Log.d("Check",quantity)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun validateQuotation(context: Context): Boolean {
        val selectedCam = selectedCamera
        var qty = quantity.toIntOrNull()

        when {
            selectedCam == null -> {
                Toast.makeText(context, "Please select a camera", Toast.LENGTH_SHORT).show()
            }
            qty == null || qty <= 0 -> {
                Toast.makeText(context, "Enter a valid quantity", Toast.LENGTH_SHORT).show()
            }
            else -> {
                if (qty > selectedCam.quantity) {
                    Toast.makeText(
                        context,
                        "Only ${selectedCam.quantity} cameras available",
                        Toast.LENGTH_SHORT
                    ).show()
                    qty = selectedCam.quantity
                    quantity = qty.toString()
                }
                return true
            }

        }
        return false
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    fun addNewCameraButtonClicked(context: Context) {
        selectedCamera?.let { camera ->
            val quantityInt = quantity.toIntOrNull()
            if (quantityInt != null) {
                quotation = quotation.copy(
                    camera = quotation.camera.toMutableList().apply { add(camera) },
                    quantity = quotation.quantity.toMutableList().apply { add(quantityInt) }
                )
                selectedCamera= null
                quantity=""
            } else {
                Toast.makeText(context, "Invalid quantity", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(context, "Select a camera", Toast.LENGTH_SHORT).show()
        }
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    fun continueButtonClicked(context: Context) {
        if (validateQuotation(context)) {
            Router.navigateTo(Screen.CustomerDetailScreen(quotation))
        }
    }
    fun removeCamera(index: Int){
        if (index >= 0 && index < quotation.camera.size) {
            quotation = quotation.copy(
                camera = quotation.camera.toMutableList().apply { removeAt(index) },
                quantity = quotation.quantity.toMutableList().apply { removeAt(index) } //Remove quantity as well.
            )
        }
    }
}
