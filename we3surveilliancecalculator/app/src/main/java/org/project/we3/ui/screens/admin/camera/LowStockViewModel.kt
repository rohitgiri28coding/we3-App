package org.project.we3.ui.screens.admin.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.project.we3.data.model.Camera
import org.project.we3.domain.usecase.camera.SortAndFilterCameraUseCase
import javax.inject.Inject

@HiltViewModel
class LowStockViewModel @Inject constructor(private val sortAndFilterCameraUseCase: SortAndFilterCameraUseCase) : ViewModel() {
    private val _lowStockCameras = MutableStateFlow<List<Camera>>(emptyList())
    val lowStockCameras: StateFlow<List<Camera>> = _lowStockCameras

    fun addLowStockCamera(cameras: List<Camera>) {
        viewModelScope.launch(Dispatchers.IO) {
            _lowStockCameras.emit(
                sortAndFilterCameraUseCase
                    .filterAndSortLowStockCameras(
                        cameras
                    )
            )
        }
    }

}