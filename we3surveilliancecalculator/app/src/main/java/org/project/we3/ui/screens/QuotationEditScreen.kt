package org.project.we3.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import org.project.we3.app.db.CameraViewModel
import org.project.we3.app.db.Quotation

@Composable
fun QuotationEditScreen(
    innerPaddingValues: PaddingValues,
    quotation: Quotation,
    cameraViewModel: CameraViewModel,
    viewModel: QuotationEditViewModel = viewModel()
){
    val currentQuotation by viewModel.currentQuotation.collectAsState()
    val showAddCameraDialog by viewModel.showAddCameraDialog.collectAsState()
    val newCameraQuantity by viewModel.newCameraQuantity.collectAsState()

    LaunchedEffect(quotation) {
        viewModel.fetchCameraFromDB(cameraViewModel.cameras.value)
        viewModel.setQuotation(quotation)
    }

    Column(modifier = Modifier.padding(innerPaddingValues)) {

    LazyColumn(modifier = Modifier.weight(1f)) {
            itemsIndexed(currentQuotation.camera) { index, camera ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = camera.name.substring(0, 4), modifier = Modifier.weight(1f))
                    cameraEditSection(
                        textValue = "Quantity",
                        value = currentQuotation.quantity.getOrElse(index) { 0 }.toString(),
                    )
                    IconButton(onClick = { viewModel.removeCamera(index) }) {
                        Icon(Icons.Filled.Close, contentDescription = "Remove", tint = Color.Red)
                    }
                }
            }
        }

        Button(onClick = { viewModel.setShowAddCameraDialog(true) }, modifier = Modifier.fillMaxWidth()) {
            Text("+ Add Camera")
        }
    }

    if (showAddCameraDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.setShowAddCameraDialog(false) },
            title = { Text("Add Camera") },
            text = {
                Column {
                    CameraDropdownMenu(cameraList = viewModel.cameras, selectedCamera = null/*TODO*/, onCameraSelected = { camera ->
                        viewModel.setSelectedCamera(camera)
                    }
                    )
                    TextField(
                        value = newCameraQuantity,
                        onValueChange = { viewModel.setNewCameraQuantity(it) },
                        label = { Text("Quantity") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                }
            },
            confirmButton = {
                Button(onClick = { viewModel.addCamera() }) {
                    Text("Add")
                }
            },
            dismissButton = {
                Button(onClick = { viewModel.setShowAddCameraDialog(false) }) {
                    Text("Cancel")
                }
            }
        )
    }

}
