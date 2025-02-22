package org.project.we3.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.project.we3.app.Camera
import org.project.we3.app.db.CameraViewModel
import org.project.we3.app.repository.FirestoreDBRepository
import javax.inject.Inject

@HiltViewModel
class AddCameraViewModel @Inject constructor(private val firestoreDBRepository: FirestoreDBRepository): ViewModel() {
    var isLoading by mutableStateOf(false)


    private fun addCameraToFirestore(
        context: Context,
        cameraViewModel: CameraViewModel,
        name: String,
        details: String,
        mrp: String,
        unitPrice: String,
        gstRate: String,
        quantity: String
    ) {
        val cameraData = Camera(
            name = name,
            detail = details,
            mrp = mrp.toDouble(),
            unitPrice = unitPrice.toDouble(),
            gst = gstRate.toDouble(),
            quantity = quantity.toInt()
        )
        viewModelScope.launch {
            firestoreDBRepository.addCamera(cameraData, context, cameraViewModel)
        }
    }

    fun checkDataAndUpload(context: Context, cameraViewModel: CameraViewModel, cameraName: String, cameraDetails: String, mrp: String, unitPrice: String, gstRate: String, quantity: String) {
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
            addCameraToFirestore(context, cameraViewModel, cameraName, cameraDetails, mrp, unitPrice, gstRate, quantity)
        }
        isLoading = false
    }

}