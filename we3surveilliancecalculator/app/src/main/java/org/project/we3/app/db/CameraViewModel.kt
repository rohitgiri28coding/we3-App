package org.project.we3.app.db

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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

    val cameras: StateFlow<List<Camera>> = repository.cameras.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptyList()
    )

    init {
        refreshCameras()
    }

    fun refreshCameras() {
        viewModelScope.launch {
            val cameraList = firestoreDBRepository.fetchCamera()
            repository.refreshCameras(cameraList)
        }
    }
}
