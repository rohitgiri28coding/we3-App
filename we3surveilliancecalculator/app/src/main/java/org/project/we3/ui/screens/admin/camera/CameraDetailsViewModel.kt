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
import org.project.we3.domain.usecase.camera.RemoveCameraUseCase
import org.project.we3.ui.navigation.Router
import org.project.we3.ui.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class CameraDetailsViewModel @Inject constructor(private val removeCameraUseCase: RemoveCameraUseCase): ViewModel() {

    var isLoading by mutableStateOf(false)

    fun deleteCamera(camera: Camera, context: Context, cameraViewModel: CameraViewModel) {
        viewModelScope.launch {
            removeCameraUseCase.invoke(camera.firestoreId)
            withContext (Dispatchers.IO){
                cameraViewModel.refreshCameras()
                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()
            }
        }
        isLoading = false
    }

    fun navigateToEditScreen(camera: Camera) {
        Router.navigateTo(Screen.EditScreen(camera))
    }

}