package org.project.we3.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.project.we3.app.db.User
import org.project.we3.app.repository.AdminAuth
import org.project.we3.app.repository.AdminPreferences
import javax.inject.Inject

@HiltViewModel
class AdminLoginViewModel @Inject constructor(
    application: Application,
    private val adminAuth: AdminAuth
) : AndroidViewModel(application) {

    private val adminPreferences = AdminPreferences(application)

    private val _isAdminLoggedIn = MutableStateFlow(false)
    val isAdminLoggedIn: StateFlow<Boolean> = _isAdminLoggedIn.asStateFlow()

    init {
        viewModelScope.launch {
            _isAdminLoggedIn.value = adminPreferences.isAdminLoggedIn.first()
        }
    }

    fun loginAdmin(email: String, password: String) {
        viewModelScope.launch {
            val isAdmin = adminAuth.checkIsAdmin(User(email, password)) // Wait for Firestore result
            if (isAdmin) {
                _isAdminLoggedIn.value = true
                adminPreferences.setAdminLoggedIn(true)
            }
        }
    }
}
