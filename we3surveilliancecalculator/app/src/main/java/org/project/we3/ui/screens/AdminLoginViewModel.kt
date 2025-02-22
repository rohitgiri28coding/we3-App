package org.project.we3.ui.screens

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.project.we3.app.AdminCredentials
import javax.inject.Inject

@HiltViewModel
class AdminLoginViewModel @Inject constructor() : ViewModel() {
    var isAdminLoggedIn: Boolean = false

    fun login(email: String, password: String): Boolean {

        if (email.trim() == AdminCredentials.ADMIN_EMAIL && password == AdminCredentials.ADMIN_PASSWORD) {
            isAdminLoggedIn = true
            return true
        }
        return false
    }

}
