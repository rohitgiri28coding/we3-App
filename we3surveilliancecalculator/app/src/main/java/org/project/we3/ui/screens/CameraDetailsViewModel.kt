package org.project.we3.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.project.we3.app.Camera
import org.project.we3.app.db.CameraViewModel
import org.project.we3.app.navigation.Router
import org.project.we3.app.navigation.Screen
import org.project.we3.app.repository.FirestoreDBRepository
import javax.inject.Inject

@HiltViewModel
class CameraDetailsViewModel @Inject constructor(private val firestoreDBRepository: FirestoreDBRepository): ViewModel() {

    var isLoading by mutableStateOf(false)

    fun deleteCamera(camera: Camera, context: Context, cameraViewModel: CameraViewModel) {
        Log.d("Firestore", "Deleting camera: $camera")
        viewModelScope.launch {
            firestoreDBRepository.deleteCamera(camera, context, cameraViewModel)
        }
        isLoading = false
    }

    fun navigateToEditScreen(camera: Camera) {
        Router.navigateTo(Screen.EditScreen(camera))
    }

}