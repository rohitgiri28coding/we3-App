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
import org.project.we3.ui.screens.AdminLoginScreen
import org.project.we3.ui.screens.AdminLoginViewModel
import org.project.we3.ui.screens.CameraDetailsScreen
import org.project.we3.ui.screens.CameraEditScreen
import org.project.we3.ui.screens.SurveillanceCalculatorScreen
import org.project.we3.ui.screens.ViewAllCameraScreen


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun AppScreenNavigation(
    innerPadding: PaddingValues,
    loginViewModel: AdminLoginViewModel = viewModel(),
    cameraViewModel: CameraViewModel = viewModel()
) {
    val cameraList by cameraViewModel.cameras.collectAsState(initial = emptyList())

    Crossfade(targetState = Router.currentScreen, label = "") { currentState ->
        when (currentState.value) {
            is Screen.CameraDetailScreen -> {
                val camera = (currentState.value as Screen.CameraDetailScreen).camera
                CameraDetailsScreen(camera, innerPadding, cameraViewModel)
            }
            Screen.AddNewCameraScreen -> AddCameraScreen(innerPadding, cameraViewModel)
            Screen.HomeScreen -> SurveillanceCalculatorScreen(cameraViewModel.cameras.collectAsState(initial = emptyList()).value, innerPadding)
            Screen.ViewAllCameraScreen -> {
                if (loginViewModel.isAdminLoggedIn) {
                    ViewAllCameraScreen(cameraList, innerPadding)
                } else {
                    Router.navigateTo(Screen.AdminLoginScreen) // Navigate to login if not admin
                }
            }
            is Screen.EditScreen -> {
                val camera = (currentState.value as Screen.EditScreen).camera
                CameraEditScreen(camera, innerPadding, cameraViewModel)
            }
            is Screen.AdminLoginScreen -> {
                AdminLoginScreen(
                    onLoginSuccess = {Router.navigateTo(Screen.ViewAllCameraScreen)}
                    )
            }
        }
    }
}

