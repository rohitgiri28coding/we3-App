package org.project.we3.ui.screens.admin.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.project.we3.data.model.User
import org.project.we3.data.repository.AdminAuth
import org.project.we3.data.repository.AdminPreferences
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

    fun loginAdmin(email: String, password: String, onLoginResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isAdmin = adminAuth.checkIsAdmin(User(email, password))
            if (isAdmin) {
                _isAdminLoggedIn.value = true
                adminPreferences.setAdminLoggedIn(true)
            }
            onLoginResult(isAdmin)
        }
    }

}