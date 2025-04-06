package org.project.we3.ui.screens.admin.camera

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.project.we3.data.model.Camera
import org.project.we3.domain.repository.CameraViewModel
import org.project.we3.ui.navigation.Router
import org.project.we3.ui.navigation.Screen
import org.project.we3.ui.navigation.SystemBackButtonHandler
import org.project.we3.ui.screens.CustomAlertDialogBox

@Composable
fun CameraDetailsScreen(
    camera: Camera,
    innerPaddingValues: PaddingValues,
    cameraViewModel: CameraViewModel,
    cameraDetailsViewModel: CameraDetailsViewModel = viewModel()
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) } // State to show/hide dialog

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Camera Details", fontSize = 24.sp, fontWeight = FontWeight.Bold)

            DetailRow("Name", camera.name)
            DetailRow("MRP", "₹${camera.mrp}")
            DetailRow("Unit Price", "₹${camera.unitPrice}")
            DetailRow("GST", "${camera.gst}%")
            DetailRow("Stock", camera.quantity.toString())

            Spacer(modifier = Modifier.height(16.dp))

            if (cameraDetailsViewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = {
                        cameraDetailsViewModel.navigateToEditScreen(camera)
                    }) {
                        Text("Edit")
                    }

                    Button(
                        onClick = {
                            showDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(Color.Red)
                    ) {
                        Text("Delete")
                    }
                }
            }
        }
    }
    if (showDialog) {
        CustomAlertDialogBox(
            title = "Confirm Deletion",
            message = "Are you sure you want to delete this camera? This action cannot be undone.",
            confirmButtonText = "Delete",
            confirmButtonClicked = {
                showDialog = false
                cameraDetailsViewModel.isLoading = true
                cameraDetailsViewModel.deleteCamera(camera, context, cameraViewModel)
            },
            onDismiss = {
                showDialog = false
            }
        )
    }
    SystemBackButtonHandler {
        Router.navigateTo(Screen.ViewAllCameraScreen)
    }
}

// Reusable Composable for displaying details
@Composable
fun DetailRow(label: String, value: String) {
    Column (
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        WrappedText(label, fontWeight = FontWeight.Bold, textUnit = 20.sp)
        Spacer(modifier = Modifier.height(10.dp))
        CameraDataItem(value)
    }
}
@Composable
fun WrappedText(text: String, maxLineLength: Int = 30, fontWeight: FontWeight, textUnit: TextUnit) {

    val textToDisplay = if (text.length > maxLineLength) {
        val wrappedText = buildString {
            var currentIndex = 0
            while (currentIndex < text.length) {
                val nextBreak = minOf(currentIndex + maxLineLength, text.length)
                append(text.substring(currentIndex, nextBreak))
                if (nextBreak < text.length) {
                    append("\n") // Add newline if needed
                }
                currentIndex = nextBreak
            }
        }
        wrappedText
    } else {
        text
    }

    Text(
        text = textToDisplay,
        overflow = TextOverflow.Ellipsis, // Handle overflow if necessary
        maxLines = if (text.length > maxLineLength) Int.MAX_VALUE else 1, // Important for wrapping
        textAlign = TextAlign.Center,
        fontSize = textUnit,
        fontWeight = fontWeight
    )
}
@Composable
private fun CameraDataItem(data: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Text(text = data, modifier = Modifier.padding(16.dp), fontSize = 18.sp)
    }
}

