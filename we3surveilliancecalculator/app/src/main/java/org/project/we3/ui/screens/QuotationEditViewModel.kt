package org.project.we3.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.project.we3.app.Camera
import org.project.we3.app.db.Quotation

class QuotationEditViewModel: ViewModel() {

    private val _currentQuotation = MutableStateFlow(Quotation(emptyList(), emptyList()))
    val currentQuotation: StateFlow<Quotation> = _currentQuotation.asStateFlow()

    private val _showAddCameraDialog = MutableStateFlow(false)
    val showAddCameraDialog: StateFlow<Boolean> = _showAddCameraDialog.asStateFlow()

    private val _selectedCamera = MutableStateFlow<Camera?>(null)
    val selectedCamera: StateFlow<Camera?> = _selectedCamera.asStateFlow()

    private val _newCameraQuantity = MutableStateFlow("")
    val newCameraQuantity: StateFlow<String> = _newCameraQuantity.asStateFlow()

    var cameras = emptyList<Camera>()


    fun setQuotation(quotation: Quotation) {
        viewModelScope.launch {
            _currentQuotation.emit(quotation)
        }
    }

    fun removeCamera(index: Int) {
        viewModelScope.launch {
            val updatedCameras = _currentQuotation.value.camera.filterIndexed { i, _ -> i != index }
            val updatedQuantities = _currentQuotation.value.quantity.filterIndexed { i, _ -> i != index }
            val updatedQuotation = _currentQuotation.value.copy(camera = updatedCameras, quantity = updatedQuantities)
            _currentQuotation.emit(updatedQuotation)
        }
    }

    fun fetchCameraFromDB(camera: List<Camera>) {
        cameras = camera
    }

    fun setShowAddCameraDialog(show: Boolean) {
        viewModelScope.launch {
            _showAddCameraDialog.emit(show)
        }
    }

    fun setSelectedCamera(camera: Camera?) {
        viewModelScope.launch {
            _selectedCamera.emit(camera)
        }
    }

    fun setNewCameraQuantity(quantity: String) {
        viewModelScope.launch {
            _newCameraQuantity.emit(quantity)
        }
    }

    fun addCamera() {
        viewModelScope.launch {
            if (_selectedCamera.value != null && _newCameraQuantity.value.isNotEmpty()) {
                val updatedCameras = _currentQuotation.value.camera + _selectedCamera.value!!
                val updatedQuantities = _currentQuotation.value.quantity + _newCameraQuantity.value.toInt()
                val updatedQuotation = _currentQuotation.value.copy(camera = updatedCameras, quantity = updatedQuantities)
                _currentQuotation.emit(updatedQuotation)
                _selectedCamera.emit(null)
                _newCameraQuantity.emit("")
                _showAddCameraDialog.emit(false)
            }
        }
    }
}