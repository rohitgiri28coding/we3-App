package org.project.we3.ui.screens.admin.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.project.we3.ui.navigation.Router
import org.project.we3.ui.navigation.Screen
import org.project.we3.data.repository.AdminPreferences
import javax.inject.Inject

class AdminSectionNavigatorViewModel @Inject constructor(application: Application): AndroidViewModel(application) {

    private val adminPreferences = AdminPreferences(application)

    fun navigateToAddCameraScreen() {
        Router.navigateTo(Screen.AddNewCameraScreen)
    }
    fun navigateToShowAllCameraScreen() {
        Router.navigateTo(Screen.ViewAllCameraScreen)
    }
    fun navigateToActiveQuotationScreen() {
        Router.navigateTo(Screen.ActiveQuotationListScreen)
    }
    fun navigateToFulfilledQuotationScreen() {
        Router.navigateTo(Screen.FulfilledQuotationListScreen)
    }
    fun navigateToEarningSummaryScreen() {
        Router.navigateTo(Screen.EarningSectionScreen)
    }
    fun navigateToLowStockAlertScreen() {
        Router.navigateTo(Screen.LowStockAlertScreen)
    }

    fun logoutAdmin() {
        viewModelScope.launch {
            adminPreferences.setAdminLoggedIn(false)
            withContext(Dispatchers.Main) {
                Router.navigateTo(Screen.AdminSectionNavigatorScreen)

            }
        }

    }

}