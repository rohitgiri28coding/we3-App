package org.project.we3.ui.screens

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import org.project.we3.app.navigation.Router
import org.project.we3.app.navigation.Screen

class AdminSectionNavigatorViewModel: ViewModel() {
    val db = Firebase.firestore

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




}