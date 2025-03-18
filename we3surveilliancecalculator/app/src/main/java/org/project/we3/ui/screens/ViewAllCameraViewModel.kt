package org.project.we3.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.project.we3.app.Camera
import org.project.we3.app.navigation.Router
import org.project.we3.app.navigation.Screen

class ViewAllCameraViewModel: ViewModel() {
    var isLoading by  mutableStateOf(false)

    fun navigateToCameraDetailScreen(camera: Camera) {
        Router.navigateTo(Screen.CameraDetailScreen(camera))
    }
}