package org.project.we3.ui.screens.admin.camera

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import org.project.we3.data.model.Camera
import org.project.we3.domain.repository.CameraViewModel
import org.project.we3.ui.navigation.Router
import org.project.we3.ui.navigation.Screen
import org.project.we3.ui.navigation.SystemBackButtonHandler
import org.project.we3.ui.screens.CardEditSection

@Composable
fun CameraEditScreen(camera: Camera, innerPaddingValues: PaddingValues, cameraViewModel: CameraViewModel, cameraEditViewModel: CameraEditViewModel = hiltViewModel()) {

    val context = LocalContext.current

    LaunchedEffect(camera) {
        cameraEditViewModel.setCamera(camera)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues),
        contentAlignment = Alignment.Center
    ) {
        if(cameraEditViewModel.isLoading){
            CircularProgressIndicator()
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ){
            CardEditSection("Camera Name", cameraEditViewModel.cameraName, KeyboardType.Text) {
                cameraEditViewModel.updateCameraName(it)
            }
            CardEditSection("MRP", cameraEditViewModel.mrp) {
                cameraEditViewModel.updateMRP(it)
            }
            CardEditSection("Unit Price (Without GST)", cameraEditViewModel.unitPrice) {
                cameraEditViewModel.updateUnitPrice(it)
            }
            CardEditSection("GST", cameraEditViewModel.gstRate) {
                cameraEditViewModel.updateGSTRate(it)
            }
            CardEditSection("Quantity", cameraEditViewModel.quantity) {
                cameraEditViewModel.updateQuantity(it)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    cameraEditViewModel.showDialog = true
                })
                {
                    Text("Update", fontSize = 20.sp)
                }
            }
        }
    }
    if (cameraEditViewModel.showDialog) {
        AlertDialog(
            onDismissRequest = { cameraEditViewModel.showDialog = false },
            title = { Text("Confirm Edit") },
            text = { Text("Are you sure you want to edit details of this camera? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        cameraEditViewModel.showDialog = false
                        cameraEditViewModel.isLoading = true
                        cameraEditViewModel.checkDataAndUpdate(
                            context = context,
                            cameraViewModel = cameraViewModel,
                            id = camera.firestoreId, // Preserve the original ID
                        )
                    },
                    colors = ButtonDefaults.buttonColors(Color.Red)
                ) {
                    Text("Edit")
                }
            },
            dismissButton = {
                Button(onClick = { cameraEditViewModel.showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    SystemBackButtonHandler {
        Router.navigateTo(Screen.ViewAllCameraScreen)
    }
}

