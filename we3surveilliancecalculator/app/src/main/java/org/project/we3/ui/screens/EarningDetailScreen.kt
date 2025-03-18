package org.project.we3.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.project.we3.app.Earning
import org.project.we3.app.formatNumberIntoIndianNumber

@Composable
fun EarningDetailScreen(innerPadding: PaddingValues, earning: Earning, earningDetailViewModel: EarningDetailViewModel= viewModel()){
    val context = LocalContext.current
    val quotation = earningDetailViewModel.quotations.collectAsState().value

    LaunchedEffect(earning) {
        Log.d("EarningDetailScreen", "Fetching quotation for earning: $earning")
        earningDetailViewModel.fetchQuotation(earning.quotationId)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Earning Details", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            DetailRow("Earned On", earning.date)
            DetailRow("Customer Name", quotation.customerName)
            DetailRow("Phone Number", quotation.phoneNumber)
            quotation.camera.forEachIndexed { index, camera ->
                DetailRow("Camera Name & Quantity", "${camera.name} (${quotation.quantity[index]})")
            }
            DetailRow("Prepayment", "₹"+formatNumberIntoIndianNumber(earning.prepayment))
            DetailRow("Total Earning", "₹"+formatNumberIntoIndianNumber(earning.totalEarning))

            Button(onClick = {
                earningDetailViewModel.showPDF(context, quotation)
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Show Quotation in PDF")
            }
        }
    }
}