package org.project.we3.ui.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import org.project.we3.domain.repository.CameraViewModel
import org.project.we3.ui.screens.admin.quotation.active.ActiveQuotationListScreen
import org.project.we3.ui.screens.admin.camera.AddCameraScreen
import org.project.we3.ui.screens.admin.login.AdminLoginScreen
import org.project.we3.ui.screens.admin.login.AdminLoginViewModel
import org.project.we3.ui.screens.admin.login.AdminSectionNavigatorScreen
import org.project.we3.ui.screens.admin.camera.CameraDetailsScreen
import org.project.we3.ui.screens.admin.camera.CameraEditScreen
import org.project.we3.ui.screens.contact.ContactUsScreen
import org.project.we3.ui.screens.calculator.CustomerDetailScreen
import org.project.we3.ui.screens.admin.earning.EarningDetailScreen
import org.project.we3.ui.screens.admin.earning.EarningSectionScreen
import org.project.we3.ui.screens.admin.quotation.fulfilled.FulfilledQuotationListScreen
import org.project.we3.ui.screens.admin.camera.LowStockScreen
import org.project.we3.ui.screens.admin.quotation.QuotationDetailScreen
import org.project.we3.ui.screens.admin.quotation.QuotationEditScreen
import org.project.we3.ui.screens.calculator.SurveillanceCalculatorScreen
import org.project.we3.ui.screens.admin.camera.ViewAllCameraScreen

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
            Screen.ViewAllCameraScreen -> ViewAllCameraScreen(cameraList, innerPadding, cameraViewModel)

            is Screen.EditScreen -> {
                val camera = (currentState.value as Screen.EditScreen).camera
                CameraEditScreen(camera, innerPadding, cameraViewModel)
            }
            is Screen.AdminLoginScreen -> {
                AdminLoginScreen(
                    onLoginSuccess = {Router.navigateTo(Screen.AdminSectionNavigatorScreen)}
                    )
            }

            Screen.ContactScreen -> ContactUsScreen()
            is Screen.CustomerDetailScreen -> {
                val quotation = (currentState.value as Screen.CustomerDetailScreen).quotation
                CustomerDetailScreen(innerPadding, quotation)
            }

            is Screen.QuotationDetailScreen -> {
                val quotation = (currentState.value as Screen.QuotationDetailScreen).quotation
                QuotationDetailScreen(innerPadding, quotation, cameraViewModel)
            }
            is Screen.QuotationEditScreen -> {
                val quotation = (currentState.value as Screen.QuotationEditScreen).quotation
                QuotationEditScreen(innerPadding, quotation, cameraViewModel)
            }
            is Screen.ActiveQuotationListScreen -> {
                ActiveQuotationListScreen(innerPadding)
            }

            Screen.AdminSectionNavigatorScreen -> {
                if (isAdminLoggedIn) {
                    AdminSectionNavigatorScreen(innerPadding)
                } else {
                    Router.navigateTo(Screen.AdminLoginScreen) // Navigate to login if not admin
                }
            }

            Screen.EarningSectionScreen -> EarningSectionScreen(innerPadding)
            is Screen.FulfilledQuotationListScreen -> {
                FulfilledQuotationListScreen(innerPadding)
            }

            is Screen.EarningDetailScreen -> {
                val earning = (currentState.value as Screen.EarningDetailScreen).earning
                EarningDetailScreen(innerPadding, earning)
            }
            Screen.LowStockAlertScreen -> {
                LowStockScreen(cameraList, cameraViewModel, innerPadding)
            }
        }
    }
}




