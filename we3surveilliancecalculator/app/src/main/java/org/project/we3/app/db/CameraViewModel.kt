package org.project.we3.app.db

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.project.we3.app.Camera
import org.project.we3.app.repository.FirestoreDBRepository
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val repository: CameraRepository,
    private val firestoreDBRepository: FirestoreDBRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    val cameras: StateFlow<List<Camera>> = repository.cameras
        .onEach { _isLoading.value = false } // Mark loading as false when data is available
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )

    init {
        refreshCameras()
    }

    fun refreshCameras() {
        viewModelScope.launch {
            _isLoading.value = true  // Start loading
            val cameraList = firestoreDBRepository.fetchCamera()
            repository.refreshCameras(cameraList)
            _isLoading.value = false // Stop loading after fetching data
        }
    }
}
