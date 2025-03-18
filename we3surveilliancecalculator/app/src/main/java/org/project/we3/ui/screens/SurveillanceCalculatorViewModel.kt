package org.project.we3.ui.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
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

    var quotation by mutableStateOf(Quotation(camera = emptyList<Camera>(), quantity = emptyList()))

    fun selectCamera(camera: Camera) {
        selectedCamera = camera
    }

    fun updateQuantity(newQuantity: String) {
        Log.d("Check",quantity)
        quantity = newQuantity
        Log.d("Check",quantity)
    }
    fun addNewCameraButtonClicked(context: Context) {
        selectedCamera?.let { camera ->
            var quantityInt = quantity.toIntOrNull()
            if (quantityInt != null) {
                val existingIndex = quotation.camera.indexOfFirst { it.name == camera.name }

                if (existingIndex != -1) {
                    // Camera already exists, update quantity
                    val currentQuantity = quotation.quantity[existingIndex]
                    val maxQuantity = camera.quantity

                    if (currentQuantity + quantityInt > maxQuantity) {
                        Toast.makeText(context, "Maximum available quantity for ${camera.name} is ${maxQuantity}.", Toast.LENGTH_SHORT).show()
                        quantityInt = maxQuantity - currentQuantity
                        if(quantityInt < 1){
                            Toast.makeText(context, "No more ${camera.name} cameras can be added", Toast.LENGTH_SHORT).show()
                            return
                        }
                        quantity = quantityInt.toString()
                    }

                    val updatedQuantity = currentQuantity + quantityInt
                    val updatedCameraList = quotation.camera.toMutableList()
                    val updatedQuantityList = quotation.quantity.toMutableList()

                    updatedQuantityList[existingIndex] = updatedQuantity
                    quotation = quotation.copy(camera = updatedCameraList, quantity = updatedQuantityList)

                } else {
                    // Camera is new, add it
                    if (quantityInt > camera.quantity){
                        Toast.makeText(context, "Maximum available quantity for ${camera.name} is ${camera.quantity}.", Toast.LENGTH_SHORT).show()
                        quantityInt = camera.quantity
                        quantity = quantityInt.toString()
                    }

                    quotation = quotation.copy(
                        camera = quotation.camera.toMutableList().apply { add(camera) },
                        quantity = quotation.quantity.toMutableList().apply { add(quantityInt) }
                    )
                }

                selectedCamera = null
                quantity = ""
            } else {
                Toast.makeText(context, "Invalid quantity", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(context, "Select a camera", Toast.LENGTH_SHORT).show()
        }
    }

    fun continueButtonClicked(context: Context) {
        selectedCamera?.let {
            if (quantity.isNotEmpty()) {
                addNewCameraButtonClicked(context)
            }
        }
        if(quotation.camera.isNotEmpty()) {
            Router.navigateTo(Screen.CustomerDetailScreen(quotation))
        }else{
            Toast.makeText(context, "Please add at least one camera", Toast.LENGTH_SHORT).show()
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
