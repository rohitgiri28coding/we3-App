package org.project.we3.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import org.project.we3.app.navigation.Router
import org.project.we3.app.navigation.Screen

class AddCameraViewModel: ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    var isLoading by mutableStateOf(false)


    private fun addCameraToFirestore(
        context: Context,
        name: String,
        details: String,
        mrp: String,
        unitPrice: String,
        gstRate: String,
        quantity: String
    ) {
        val cameraData = hashMapOf(
            "name" to name,
            "details" to details,
            "mrp" to mrp.toDoubleOrNull(),
            "unitPrice" to unitPrice.toDoubleOrNull(),
            "gst" to gstRate.toDoubleOrNull(),
            "quantity" to quantity.toIntOrNull()
        )

        db.collection("CameraList")
            .add(cameraData)
            .addOnSuccessListener {
                Toast.makeText(context, "Camera added successfully!", Toast.LENGTH_SHORT).show()
                Router.navigateTo(Screen.ViewAllCameraScreen)
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error adding camera", Toast.LENGTH_SHORT).show()
            }
    }

    fun checkDataAndUpload(context: Context, cameraName: String, cameraDetails: String, mrp: String, unitPrice: String, gstRate: String, quantity: String) {
        isLoading = true
        if(cameraName.isEmpty() || cameraDetails.isEmpty() || mrp.isEmpty() || unitPrice.isEmpty() || gstRate.isEmpty() || quantity.isEmpty()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
        else{
            if(mrp.toDoubleOrNull() == null || unitPrice.toDoubleOrNull() == null || gstRate.toDoubleOrNull() == null || quantity.toIntOrNull() == null) {
                Toast.makeText(context, "Please enter valid data", Toast.LENGTH_SHORT).show()
                isLoading = false
                return
            }
            addCameraToFirestore(context, cameraName, cameraDetails, mrp, unitPrice, gstRate, quantity)
        }
        isLoading = false
    }

}