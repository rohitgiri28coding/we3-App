package org.project.we3.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CameraViewModel(application: Application) : AndroidViewModel(application) {

    private val database = CameraDatabase.getDatabase(application)
    private val repository = CameraRepository(database.cameraDao())

    val cameras: StateFlow<List<Camera>> = repository.cameras.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptyList()
    )

    fun fetchFromFirestore() {
        viewModelScope.launch {
            repository.refreshCameras()
        }
    }
}
