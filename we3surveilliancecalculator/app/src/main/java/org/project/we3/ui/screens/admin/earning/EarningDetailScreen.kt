package org.project.we3.ui.screens.admin.earning

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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.hilt.navigation.compose.hiltViewModel
import org.project.we3.app.formatNumberIntoIndianNumber
import org.project.we3.data.model.Earning
import org.project.we3.ui.navigation.Router
import org.project.we3.ui.navigation.Screen
import org.project.we3.ui.navigation.SystemBackButtonHandler
import org.project.we3.ui.screens.admin.camera.DetailRow

@Composable
fun EarningDetailScreen(innerPadding: PaddingValues, earning: Earning, earningDetailViewModel: EarningDetailViewModel = hiltViewModel()){
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
        if (earningDetailViewModel.isLoading){
            CircularProgressIndicator()
        }
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
            DetailRow("Prepayment", "₹" + formatNumberIntoIndianNumber(earning.prepayment))
            DetailRow("Total Earning", "₹" + formatNumberIntoIndianNumber(earning.totalEarning))

            Button(onClick = {
                earningDetailViewModel.isLoading = true
                earningDetailViewModel.showPDF(context, quotation)
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Show Quotation in PDF")
            }
        }
    }
    SystemBackButtonHandler {
        Router.navigateTo(Screen.EarningSectionScreen)
    }
}