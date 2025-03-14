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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

    var name by remember { mutableStateOf(camera.name) }
    var details by remember { mutableStateOf(camera.detail) }
    var mrp by remember { mutableStateOf(camera.mrp.toString()) }
    var unitPrice by remember { mutableStateOf(camera.unitPrice.toString()) }
    var gst by remember { mutableStateOf(camera.gst.toString()) }
    var quantity by remember { mutableStateOf(camera.quantity.toString()) }
    var showDialog by remember { mutableStateOf(false) } // State to show/hide dialog

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp).fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = Alignment.CenterHorizontally) {
            name = cameraEditSection("Camera Name", name, KeyboardType.Text)
            details = cameraEditSection("Details", details, KeyboardType.Text)
            mrp = cameraEditSection("MRP", mrp)
            unitPrice = cameraEditSection("Unit Price (Without GST)", unitPrice)
            gst = cameraEditSection("GST", gst)
            quantity = cameraEditSection("Quantity", quantity)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    showDialog = true
                })
                {
                    Text("Update", fontSize = 20.sp)
                }
            }
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Edit") },
            text = { Text("Are you sure you want to edit details of this camera? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        editCameraViewModel.isLoading = true
                        editCameraViewModel.checkDataAndUpdate(
                            context = context,
                            cameraViewModel = cameraViewModel,
                            id = camera.firestoreId, // Preserve the original ID
                            cameraName = name,
                            unitPrice = unitPrice,
                            cameraDetails = details,
                            mrp = mrp ,
                            gstRate = gst ,
                            quantity = quantity
                        )
                    },
                    colors = ButtonDefaults.buttonColors(Color.Red)
                ) {
                    Text("Edit")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    SystemBackButtonHandler {
        Router.navigateTo(Screen.ViewAllCameraScreen)
    }
}

