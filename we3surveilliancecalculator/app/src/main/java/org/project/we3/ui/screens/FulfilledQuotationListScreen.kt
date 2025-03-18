package org.project.we3.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.project.we3.R
import org.project.we3.app.navigation.Router
import org.project.we3.app.navigation.Screen
import org.project.we3.app.navigation.SystemBackButtonHandler

@Composable
fun FulfilledQuotationListScreen(innerPadding: PaddingValues, fulfilledQuotationViewModel: FulfilledQuotationViewModel = viewModel()) {
    LaunchedEffect(Unit) {
        fulfilledQuotationViewModel.fetchFulfilledQuotations()
    }
    val quotations = fulfilledQuotationViewModel.fulfilledQuotations.collectAsState(emptyList()).value
    LazyColumn (modifier = Modifier.padding(innerPadding)){
        item {
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                Text("Fulfilled Quotations", modifier = Modifier.padding(16.dp), fontSize = 24.sp)
                IconButton(onClick = {fulfilledQuotationViewModel.fetchFulfilledQuotations()}) {
                    Icon(painter = painterResource(R.drawable.refresh_icon), contentDescription = "Refresh")
                }
            }
        }
        items (quotations){quotation->
            QuotationListComponent("${quotation.customerName} - ${quotation.dateGenerated}"){
                Router.navigateTo(Screen.QuotationDetailScreen(quotation))
            }
        }
    }
    SystemBackButtonHandler {
        Router.navigateTo(Screen.AdminSectionNavigatorScreen)
    }
}