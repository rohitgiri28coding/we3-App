package org.project.we3.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.project.we3.app.db.Quotation
import org.project.we3.app.navigation.Router
import org.project.we3.app.navigation.Screen

@Composable
fun QuotationDetailScreen(innerPaddingValues: PaddingValues, quotation: Quotation) {
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
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.padding(8.dp)) {
                Text("Quotation Details", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                if (quotation.priority){
                    Canvas(modifier = Modifier.size(5.dp)) {
                        drawCircle(
                            color = Color.Red,
                            radius = size.minDimension / 4,
                        )
                    }
                }
            }
            DetailRow("Name", quotation.customerName)
            DetailRow("Phone Number", quotation.phoneNumber)
            quotation.camera.forEachIndexed { index, camera->
               DetailRow("Camera Name & Quantity", "${camera.name} (${quotation.quantity[index]})")
            }
            DetailRow("Date Generated", quotation.dateGenerated)
            DetailRow("Valid till", quotation.validity)
            DetailRow("Prepayment", quotation.prepaymentAmount.toString())
            DetailRow("Extra Discount", quotation.discountAmount.toString())

            Spacing()
            OutlinedButton(onClick = {
                Router.navigateTo(Screen.QuotationEditScreen(quotation))
            }) {
                Text("Edit")
            }
            Button(onClick = {}) {
                Text("Delete")
            }
            Button(onClick = {
                
            }) {
                Text("Completed")
            }

        }
    }
}