package org.project.we3.ui.screens.calculator

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.project.we3.data.model.Camera
import org.project.we3.data.model.Quotation
import org.project.we3.ui.navigation.Router
import org.project.we3.ui.navigation.Screen

class SurveillanceCalculatorViewModel: ViewModel(){
    var isLoading by mutableStateOf(true)
    var selectedCamera by mutableStateOf<Camera?>(null)
    var quantity by mutableStateOf("")

    var quotation by mutableStateOf(Quotation())

    fun selectCamera(camera: Camera){
        selectedCamera = camera
    }

    fun updateQuantity(newQuantity: String) {
        quantity = newQuantity
    }
    fun addNewCameraButtonClicked(context: Context) {
        selectedCamera?.let { camera ->
            var quantityInt = quantity.toIntOrNull()
            if (quantityInt == null || quantityInt <= 0) {
                Toast.makeText(context, "Invalid quantity", Toast.LENGTH_SHORT).show()
                return
            }

            val existingIndex = quotation.camera.indexOfFirst { it.name == camera.name }

            if (existingIndex != -1) {
                // Camera already exists, update quantity
                val currentQuantity = quotation.quantity[existingIndex]
                val maxQuantity = camera.quantity

                if (currentQuantity + quantityInt > maxQuantity) {
                    Toast.makeText(
                        context,
                        "Maximum available quantity for ${camera.name} is $maxQuantity.",
                        Toast.LENGTH_SHORT
                    ).show()

                    quantityInt = maxQuantity - currentQuantity
                    if (quantityInt < 1) {
                        Toast.makeText(
                            context,
                            "No more ${camera.name} cameras can be added",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }
                    quantity = quantityInt.toString()
                }

                // Update quantity
                val updatedQuantityList = quotation.quantity.toMutableList()
                updatedQuantityList[existingIndex] = currentQuantity + quantityInt
                quotation = quotation.copy(quantity = updatedQuantityList)
            } else {
                // Camera is new, add it
                if (quantityInt > camera.quantity) {
                    Toast.makeText(
                        context,
                        "Maximum available quantity for ${camera.name} is ${camera.quantity}.",
                        Toast.LENGTH_SHORT
                    ).show()
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
        val hasRemoved = removeCameraWithZeroQuantity()
        if(quotation.camera.isNotEmpty()) {
            Router.navigateTo(Screen.CustomerDetailScreen(quotation))
            quotation = Quotation()
        }else if(!hasRemoved){
            Toast.makeText(context, "Please add at least one camera", Toast.LENGTH_SHORT).show()
        }
    }

    private fun removeCameraWithZeroQuantity(): Boolean {
        var hasRemoved = false
        quotation.camera.forEachIndexed { index, camera ->
            val quantity = quotation.quantity[index]
            if(quantity == 0){
                removeCamera(quotation.camera.indexOf(camera))
                hasRemoved = true
            }
        }
        return hasRemoved
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
