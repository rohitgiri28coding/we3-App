package org.project.we3.ui.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import org.project.we3.data.model.Camera
import org.project.we3.data.model.Earning
import org.project.we3.data.model.Quotation

sealed class Screen {
    data class EditScreen(val camera: Camera) : Screen()
    data object AddNewCameraScreen : Screen()
    data class CameraDetailScreen(val camera: Camera) : Screen()
    data object AdminLoginScreen : Screen()
    data object ViewAllCameraScreen : Screen()
    data object HomeScreen : Screen()
    data object ContactScreen: Screen()
    data class CustomerDetailScreen(val quotation: Quotation): Screen()
    data class QuotationDetailScreen(val quotation: Quotation): Screen()
    data class QuotationEditScreen(val quotation: Quotation): Screen()
    data object ActiveQuotationListScreen: Screen()
    data object AdminSectionNavigatorScreen: Screen()
    data object EarningSectionScreen: Screen()
    data object FulfilledQuotationListScreen: Screen()
    data class EarningDetailScreen(val earning: Earning): Screen()
    data object LowStockAlertScreen: Screen()
}


object Router {
    var currentScreen: MutableState<Screen> = mutableStateOf(Screen.HomeScreen)

    fun navigateTo(destination: Screen){
        currentScreen.value = destination
    }
}