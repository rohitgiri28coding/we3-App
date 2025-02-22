package org.project.we3.app.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import org.project.we3.app.db.CameraViewModel
import org.project.we3.ui.screens.AddCameraScreen
import org.project.we3.ui.screens.CameraDetailsScreen
import org.project.we3.ui.screens.CameraEditScreen
import org.project.we3.ui.screens.SurveillanceCalculatorScreen
import org.project.we3.ui.screens.ViewAllCameraScreen

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun AppScreenNavigation (innerPadding: PaddingValues, viewModel: CameraViewModel = viewModel()) {
    val cameraList by viewModel.cameras.collectAsState(initial = emptyList())
    Crossfade(targetState = Router.currentScreen, label = "") { currentState ->
        when (currentState.value) {
            is Screen.CameraDetailScreen -> {
                val camera = (currentState.value as Screen.CameraDetailScreen).camera
                CameraDetailsScreen(camera, innerPadding)
            }
            Screen.AddNewCameraScreen -> AddCameraScreen(innerPadding)
            Screen.HomeScreen -> SurveillanceCalculatorScreen(cameraList, innerPadding)
            Screen.ViewAllCameraScreen -> ViewAllCameraScreen(innerPadding)
            is Screen.EditScreen -> {
                val camera = (currentState.value as Screen.EditScreen).camera
                CameraEditScreen(camera, innerPadding)
            }
        }
    }
}