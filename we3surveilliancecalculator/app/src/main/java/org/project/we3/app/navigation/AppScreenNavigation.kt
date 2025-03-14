package org.project.we3.app.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import org.project.we3.app.db.CameraViewModel
import org.project.we3.ui.screens.ActiveQuotationListScreen
import org.project.we3.ui.screens.AddCameraScreen
import org.project.we3.ui.screens.AdminLoginScreen
import org.project.we3.ui.screens.AdminLoginViewModel
import org.project.we3.ui.screens.CameraDetailsScreen
import org.project.we3.ui.screens.CameraEditScreen
import org.project.we3.ui.screens.ContactUsScreen
import org.project.we3.ui.screens.CustomerDetailScreen
import org.project.we3.ui.screens.QuotationDetailScreen
import org.project.we3.ui.screens.QuotationEditScreen
import org.project.we3.ui.screens.SurveillanceCalculatorScreen
import org.project.we3.ui.screens.ViewAllCameraScreen

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun AppScreenNavigation(
    innerPadding: PaddingValues,
    loginViewModel: AdminLoginViewModel = hiltViewModel(),
    cameraViewModel: CameraViewModel = hiltViewModel()
) {
    val cameraList by cameraViewModel.cameras.collectAsState(initial = emptyList())
    val isAdminLoggedIn by loginViewModel.isAdminLoggedIn.collectAsState(initial = false)

    Crossfade(targetState = Router.currentScreen, label = "") { currentState ->
        when (currentState.value) {
            is Screen.CameraDetailScreen -> {
                val camera = (currentState.value as Screen.CameraDetailScreen).camera
                CameraDetailsScreen(camera, innerPadding, cameraViewModel)
            }
            Screen.AddNewCameraScreen -> AddCameraScreen(innerPadding, cameraViewModel)
            Screen.HomeScreen -> SurveillanceCalculatorScreen(cameraViewModel.cameras.collectAsState(initial = emptyList()).value, innerPadding, cameraViewModel)
            Screen.ViewAllCameraScreen -> {
                if (isAdminLoggedIn) {
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

            Screen.ContactScreen -> ContactUsScreen()
            is Screen.CustomerDetailScreen -> {
                val quotation = (currentState.value as Screen.CustomerDetailScreen).quotation
                CustomerDetailScreen(innerPadding, quotation)
            }

            is Screen.QuotationDetailScreen -> {
                val quotation = (currentState.value as Screen.QuotationDetailScreen).quotation
                QuotationDetailScreen(innerPadding, quotation)
            }
            is Screen.QuotationEditScreen -> {
                val quotation = (currentState.value as Screen.QuotationEditScreen).quotation
                QuotationEditScreen(innerPadding, quotation, cameraViewModel)
            }
            is Screen.ActiveQuotationListScreen -> {
                val quotations = (currentState.value as Screen.ActiveQuotationListScreen).quotations
                ActiveQuotationListScreen(quotations)
            }
        }
    }
}


