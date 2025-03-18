package org.project.we3.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.project.we3.R
import org.project.we3.app.db.CameraViewModel
import org.project.we3.app.db.Quotation

@Composable
fun QuotationEditScreen(
    innerPaddingValues: PaddingValues,
    quotation: Quotation,
    cameraViewModel: CameraViewModel,
    quotationEditViewModel: QuotationEditViewModel = viewModel()
) {
    val uiState by quotationEditViewModel.uiState.collectAsState()
    val showAddCameraDialog by quotationEditViewModel.showAddCameraDialog.collectAsState()
    val newCameraQuantity by quotationEditViewModel.newCameraQuantity.collectAsState()
    val selectedCamera by quotationEditViewModel.selectedCamera.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(quotation) {
        quotationEditViewModel.fetchCameraFromDB(cameraViewModel.cameras.value)
        quotationEditViewModel.setQuotation(quotation)
    }

    Column(modifier = Modifier.padding(innerPaddingValues)) {
        LazyColumn(modifier = Modifier.padding(15.dp)) {
            itemsIndexed(uiState.quotation.camera) { index, camera ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CameraDropdownMenu(quotationEditViewModel.cameras, camera) {
                        quotationEditViewModel.updateSelectedCamera(index, it!!)
                    }
                    IconButton(onClick = { quotationEditViewModel.removeCamera(index) }) {
                        Icon(
                            painter = painterResource(R.drawable.trash_icon),
                            contentDescription = "Remove"
                        )
                    }
                }
                CardEditSection(
                    textValue = "${camera.name}'s Quantity",
                    value = uiState.quotation.quantity.getOrElse(index) { 0 }.toString()
                ) {
                    quotationEditViewModel.updateQuantity(index, it)
                }
                Spacing(6.dp)
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { quotationEditViewModel.setShowAddCameraDialog(true) },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) { Text("+ Add More Camera") }
                }
            }


            /** 📌 CUSTOMER DETAILS SECTION **/
            item {
                CardEditSection(
                    textValue = "Customer Name",
                    value = uiState.customerName,
                    keyboardType = KeyboardType.Text
                ) {
                    quotationEditViewModel.updateCustomerName(it)
                }
                Spacing(4.dp)
            }
            item {
                CardEditSection(
                    textValue = "Phone Number",
                    value = uiState.phoneNumber,
                ) {
                    quotationEditViewModel.updatePhoneNumber(it)
                }
                Spacing(4.dp)
            }
            item {
                CardEditSection(
                    textValue = "Date Generated",
                    value = uiState.dateGenerated,
                ) {}
                Spacing(4.dp)
            }
            item {
                CardEditSection(
                    textValue = "Validity",
                    value = uiState.expiryDate,
                ) {}
                Spacing(4.dp)
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = { quotationEditViewModel.showDatePicker = true }) {
                        Text("Change Date Range")
                    }
                }
                Spacing(4.dp)
            }

            item {
                CardEditSection(
                    textValue = "Prepayment Amount",
                    value = uiState.prepaymentAmount,
                ) {
                    quotationEditViewModel.updatePrepaymentAmount(it)
                }
                Spacing(4.dp)
            }
            item {
                CardEditSection(
                    textValue = "Discount Amount",
                    value = uiState.discountAmount,
                    imeAction = ImeAction.Done
                ) {
                    quotationEditViewModel.updateDiscountAmount(it)
                }
                Spacing(4.dp)
            }

            /** 📌 PRIORITY CHECKBOX **/
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CheckComponent(
                        text = "Priority",
                        checked = uiState.priority,
                    ) {
                        quotationEditViewModel.updatePriority(it)
                    }
                    Spacing(4.dp)
                }
            }
            /** 📌 SAVE BUTTON **/
            item {
                Button(
                    onClick = { quotationEditViewModel.validateAndSave(context, uiState) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save")
                }
                Spacing(6.dp)
            }
        }
    }
    if (quotationEditViewModel.showDatePicker) {

        DateRangePickerModal(
            startDate = parseDateToMillis(uiState.dateGenerated),
            endDate = parseDateToMillis(uiState.expiryDate),
            onDateRangeSelected = { dates ->
                quotationEditViewModel.updateDateGenerated(dates.first.toString())
                quotationEditViewModel.updateExpiryDate(dates.second.toString())
                quotationEditViewModel.showDatePicker = false
            },
            onDismiss = { quotationEditViewModel.showDatePicker = false }
        )
    }
    if (showAddCameraDialog) {
        AlertDialog(
            onDismissRequest = { quotationEditViewModel.setShowAddCameraDialog(false) },
            title = { Text("Add Camera") },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CameraDropdownMenu(cameraList = quotationEditViewModel.cameras, selectedCamera = selectedCamera) {
                        quotationEditViewModel.setSelectedCamera(it)
                    }
                    Spacing(15.dp)
                    TextField(
                        value = newCameraQuantity,
                        onValueChange = { quotationEditViewModel.setNewCameraQuantity(it) },
                        label = { Text("Quantity") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                }
            },
            confirmButton = { Button(onClick = { quotationEditViewModel.addCamera(context) }) { Text("Add") } }
        )
    }
}

