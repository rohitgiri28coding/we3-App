package org.project.we3.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import org.project.we3.app.navigation.Router
import org.project.we3.app.navigation.Screen

class EditCameraViewModel: ViewModel() {
    private val db = Firebase.firestore

    var isLoading by mutableStateOf(true)

    fun checkDataAndUpdate(context: Context, id: String, cameraName: String, cameraDetails: String, mrp: String, unitPrice: String, gstRate: String, quantity: String) {
        isLoading = true
        if (cameraName.isEmpty() || cameraDetails.isEmpty() || mrp.isEmpty() || unitPrice.isEmpty() || gstRate.isEmpty() || quantity.isEmpty()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
        } else {
            if (mrp.toDoubleOrNull() == null || unitPrice.toDoubleOrNull() == null || gstRate.toDoubleOrNull() == null || quantity.toIntOrNull() == null) {
                Toast.makeText(context, "Please enter valid data", Toast.LENGTH_SHORT).show()
                isLoading = false
                return
            }
            updateCameraToFirestore(
                context,
                id,
                cameraName,
                cameraDetails,
                mrp,
                unitPrice,
                gstRate,
                quantity
            )
        }
        isLoading = false
    }

    private fun updateCameraToFirestore(
        context: Context,
        id: String,
        cameraName: String,
        cameraDetails: String,
        mrp: String,
        unitPrice: String,
        gstRate: String,
        quantity: String
    ) {
        val cameraData = hashMapOf(
            "name" to cameraName,
            "details" to cameraDetails,
            "mrp" to mrp.toDoubleOrNull(),
            "unitPrice" to unitPrice.toDoubleOrNull(),
            "gst" to gstRate.toDoubleOrNull(),
            "quantity" to quantity.toIntOrNull()
        )

        db.collection("CameraList")
            .document(id)
            .set(cameraData)
            .addOnSuccessListener {
                Router.navigateTo(Screen.ViewAllCameraScreen)
                Toast.makeText(context, "Camera updated successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error updating camera", Toast.LENGTH_SHORT).show()
            }
    }

}