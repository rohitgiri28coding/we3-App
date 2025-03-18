package org.project.we3.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import org.project.we3.app.Camera
import org.project.we3.app.db.CameraViewModel
import org.project.we3.app.navigation.Router
import org.project.we3.app.navigation.Screen
import org.project.we3.app.navigation.SystemBackButtonHandler

@Composable
fun CameraEditScreen(camera: Camera, innerPaddingValues: PaddingValues, cameraViewModel: CameraViewModel, editCameraViewModel: EditCameraViewModel = hiltViewModel()) {

    val context = LocalContext.current

    LaunchedEffect(camera) {
        editCameraViewModel.setCamera(camera)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp).fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = Alignment.CenterHorizontally) {
            CardEditSection("Camera Name", editCameraViewModel.cameraName, KeyboardType.Text){
                editCameraViewModel.updateCameraName(it)
            }
            CardEditSection("Details", editCameraViewModel.cameraDetails, KeyboardType.Text){
                editCameraViewModel.updateCameraDetails(it)
            }
           CardEditSection("MRP", editCameraViewModel.mrp){
                editCameraViewModel.updateMRP(it)
           }
            CardEditSection("Unit Price (Without GST)", editCameraViewModel.unitPrice){
                editCameraViewModel.updateUnitPrice(it)
            }
            CardEditSection("GST", editCameraViewModel.gstRate){
                editCameraViewModel.updateGSTRate(it)
            }
            CardEditSection("Quantity", editCameraViewModel.quantity){
                editCameraViewModel.updateQuantity(it)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    editCameraViewModel.showDialog = true
                })
                {
                    Text("Update", fontSize = 20.sp)
                }
            }
        }
    }
    if (editCameraViewModel.showDialog) {
        AlertDialog(
            onDismissRequest = { editCameraViewModel.showDialog = false },
            title = { Text("Confirm Edit") },
            text = { Text("Are you sure you want to edit details of this camera? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        editCameraViewModel.showDialog = false
                        editCameraViewModel.isLoading = true
                        editCameraViewModel.checkDataAndUpdate(
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
                Button(onClick = { editCameraViewModel.showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    SystemBackButtonHandler {
        Router.navigateTo(Screen.ViewAllCameraScreen)
    }
}

