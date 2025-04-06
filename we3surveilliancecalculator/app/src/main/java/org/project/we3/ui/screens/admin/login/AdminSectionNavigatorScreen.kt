package org.project.we3.ui.screens.admin.login

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AdminSectionNavigatorScreen(innerPaddingValues: PaddingValues,  adminSectionNavigatorViewModel: AdminSectionNavigatorViewModel = hiltViewModel()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {

            Column {
                Spacer(modifier = Modifier.height(20.dp))
                AddCameraComponent{
                    adminSectionNavigatorViewModel.navigateToAddCameraScreen()
                }
                Spacer(modifier = Modifier.height(10.dp))
                ShowAllListComponent ("Show/Edit a camera"){
                    adminSectionNavigatorViewModel.navigateToShowAllCameraScreen()
                }
                Spacer(modifier = Modifier.height(10.dp))
                ShowAllListComponent("Active Quotations") {
                    adminSectionNavigatorViewModel.navigateToActiveQuotationScreen()
                }
                Spacer(modifier = Modifier.height(10.dp))
                ShowAllListComponent("Fulfilled Quotations") {
                    adminSectionNavigatorViewModel.navigateToFulfilledQuotationScreen()
                }
                Spacer(modifier = Modifier.height(10.dp))
                ShowAllListComponent("Earning Summary") {
                    adminSectionNavigatorViewModel.navigateToEarningSummaryScreen()
                }
                ShowAllListComponent("Low Stock Alert") {
                    adminSectionNavigatorViewModel.navigateToLowStockAlertScreen()
                }
                Spacer(modifier = Modifier.weight(1f))
//                ShowAllListComponent("Log Out") {
//                    adminSectionNavigatorViewModel.logoutAdmin()
//                }
                Spacer(modifier = Modifier.height(20.dp))

            }

        }
    }
}

@Composable
private fun AddCameraComponent(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onClick()
            },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Row(horizontalArrangement = Arrangement.SpaceAround) {
            Icon(
                Icons.Filled.AddCircle,
                "Add Icon",
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "Add a new camera",
                modifier = Modifier.padding(16.dp),
                fontSize = 18.sp
            )
        }
    }
}
@Composable
private fun ShowAllListComponent(text: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onClick()
            },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp),
            fontSize = 18.sp
        )
    }
}

