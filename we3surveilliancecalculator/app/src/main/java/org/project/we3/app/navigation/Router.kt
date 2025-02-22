package org.project.we3.app.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import org.project.we3.app.Camera

sealed class Screen {
    data class EditScreen(val camera: Camera) : Screen()
    data object AddNewCameraScreen : Screen()
    data class CameraDetailScreen(val camera: Camera) : Screen()
    data object AdminLoginScreen : Screen()
    data object ViewAllCameraScreen : Screen()
    data object HomeScreen : Screen()
}


object Router {
    var currentScreen: MutableState<Screen> = mutableStateOf(Screen.HomeScreen)

    fun navigateTo(destination: Screen){
        currentScreen.value = destination
    }
}