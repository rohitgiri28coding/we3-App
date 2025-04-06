package org.project.we3.ui.screens.admin.camera

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.project.we3.data.model.Camera
import org.project.we3.domain.repository.CameraViewModel
import org.project.we3.domain.usecase.camera.AddCameraUseCase
import javax.inject.Inject

@HiltViewModel
class AddCameraViewModel @Inject constructor(private val addCameraUseCase: AddCameraUseCase): ViewModel() {
    var isLoading by mutableStateOf(false)

    var cameraName by mutableStateOf("")
        private set
    var mrp by mutableStateOf("")
        private set
    var unitPrice by mutableStateOf("")
        private set
    var gstRate by mutableStateOf("18.0")
        private set
    var quantity by mutableStateOf("")
        private set

    private fun addCameraToFirestore(
        context: Context,
        cameraViewModel: CameraViewModel
    ) {
        val cameraData = Camera(
            name = cameraName,
            mrp = mrp.toDouble(),
            unitPrice = unitPrice.toDouble(),
            gst = gstRate.toDouble(),
            quantity = quantity.toInt()
        )
        viewModelScope.launch (Dispatchers.IO){
            addCameraUseCase.invoke(cameraData)
            withContext (Dispatchers.IO){
                cameraViewModel.refreshCameras()
                Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun checkDataAndUpload(context: Context, cameraViewModel: CameraViewModel) {
        isLoading = true
        if(cameraName.isEmpty() || mrp.isEmpty() || unitPrice.isEmpty() || gstRate.isEmpty() || quantity.isEmpty()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
        else{
            if(mrp.toDoubleOrNull() == null || unitPrice.toDoubleOrNull() == null || gstRate.toDoubleOrNull() == null || quantity.toIntOrNull() == null) {
                Toast.makeText(context, "Please enter valid data", Toast.LENGTH_SHORT).show()
                isLoading = false
                return
            }
            addCameraToFirestore(context, cameraViewModel)
        }
        isLoading = false
    }

    fun updateCameraName(name: String){
        cameraName = name
    }

    fun updateMRP(mrp: String){
        this.mrp = mrp
    }

    fun updateUnitPrice(unitPrice: String){
        this.unitPrice = unitPrice
    }

    fun updateGSTRate(gstRate: String){
        this.gstRate = gstRate
    }

    fun updateQuantity(quantity: String){
        this.quantity = quantity
    }

}