package org.project.we3.ui.screens.admin.quotation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import org.project.we3.data.model.Quotation
import org.project.we3.domain.repository.CameraViewModel
import org.project.we3.ui.navigation.Router
import org.project.we3.ui.navigation.Screen
import org.project.we3.ui.navigation.SystemBackButtonHandler
import org.project.we3.ui.screens.CustomAlertDialogBox
import org.project.we3.ui.screens.Spacing
import org.project.we3.ui.screens.admin.camera.DetailRow

@Composable
fun QuotationDetailScreen(innerPaddingValues: PaddingValues, quotation: Quotation, cameraViewModel: CameraViewModel, quotationDetailViewModel: QuotationDetailViewModel = hiltViewModel()) {
    LaunchedEffect(quotation) {
        quotationDetailViewModel.setQuotationAndCamera(quotation, cameraViewModel.cameras)
    }
    val context = LocalContext.current
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
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Quotation Details", fontSize = 24.sp, fontWeight = FontWeight.Bold)

                if (quotation.priority) {
                    Canvas(modifier = Modifier.size(30.dp)) {
                        drawCircle(
                            color = Color.Red,
                            radius = size.minDimension / 4,
                        )
                    }
                }
            }
            DetailRow("Customer Name", quotation.customerName)
            DetailRow("Phone Number", quotation.phoneNumber)
            quotation.camera.forEachIndexed { index, camera ->
                DetailRow("Camera Name & Quantity", "${camera.name} (${quotation.quantity[index]})")
            }
            DetailRow("Date Generated", quotation.dateGenerated)
            DetailRow("Valid till", quotation.expiryDate)
            DetailRow("Prepayment", quotation.prepaymentAmount.toString())
            DetailRow("Extra Discount", quotation.discountAmount.toString())
            if (!quotation.isActive) {
                DetailRow("Quotation Status", "Completed on: ${quotation.completionDate}")
            }
            Spacing()
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth().padding(5.dp)
            ) {
                Button(onClick = {
                    quotationDetailViewModel.navigateToEditScreen(quotation)
                }, modifier = Modifier.weight(0.5f)) {
                    Text("Edit")
                }
                Spacing(5.dp)

                Button(
                    onClick = {
                        quotationDetailViewModel.showDeleteDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(Color.Red),
                    modifier = Modifier.weight(0.5f)
                ) {
                    Text("Delete")
                }
            }

            Button(onClick = {
                quotationDetailViewModel.showCompletedDialog = true
            }, modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                Text(if (quotation.isActive) "Mark as Completed" else "Mark as Active")
            }

            Button(onClick = {
                quotationDetailViewModel.showLoader = true
                quotationDetailViewModel.showPDF(context, quotation)
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Show Quotation PDF")
            }
            if(quotationDetailViewModel.showDeleteDialog){
                CustomAlertDialogBox(
                    title = "Confirm Deletion",
                    message = "Are you sure you want to delete this quotation? This action cannot be undone.",
                    confirmButtonText = "Delete",
                    confirmButtonClicked = {
                        quotationDetailViewModel.showDeleteDialog = false
                        quotationDetailViewModel.showLoader = true
                        quotationDetailViewModel.removeQuotation()
                    },
                    onDismiss = {
                        quotationDetailViewModel.showDeleteDialog = false
                    }
                )
            }
            if (quotationDetailViewModel.showCompletedDialog){
                CustomAlertDialogBox(
                    title = if (quotation.isActive) "Mark as Completed" else "Mark as Active",
                    message = if (quotation.isActive) "Are you sure you want to mark this quotation as completed? This action cannot be undone." else "Are you sure you want to mark this quotation as active? This action cannot be undone.",
                    confirmButtonText = if (quotation.isActive) "Mark as Completed" else "Mark as Active",
                    confirmButtonClicked = {
                        quotationDetailViewModel.showCompletedDialog = false
                        quotationDetailViewModel.showLoader = true
                        quotationDetailViewModel.toggleActiveStatus()
                    },
                    onDismiss = {
                        quotationDetailViewModel.showCompletedDialog = false
                    }
                )
            }


        }
        if(quotationDetailViewModel.showLoader){
            CircularProgressIndicator()
        }
        SystemBackButtonHandler {
            Router.navigateTo(Screen.AdminSectionNavigatorScreen)
        }
    }
}

