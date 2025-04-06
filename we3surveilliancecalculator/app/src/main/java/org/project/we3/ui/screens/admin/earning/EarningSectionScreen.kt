package org.project.we3.ui.screens.admin.earning

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
import androidx.hilt.navigation.compose.hiltViewModel
import org.project.we3.R
import org.project.we3.app.formatNumberIntoIndianNumber
import org.project.we3.ui.navigation.Router
import org.project.we3.ui.navigation.Screen
import org.project.we3.ui.navigation.SystemBackButtonHandler
import org.project.we3.ui.screens.QuotationListComponent

@Composable
fun EarningSectionScreen(innerPadding: PaddingValues, earningSectionViewModel: EarningSectionViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        earningSectionViewModel.fetchEarningList()
    }
    val earningList = earningSectionViewModel.earningList.collectAsState(emptyList()).value
    LazyColumn (modifier = Modifier.padding(innerPadding)){
        item {
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                Text("Earning Summary", modifier = Modifier.padding(16.dp), fontSize = 24.sp)
                IconButton(onClick = {earningSectionViewModel.fetchEarningList()}) {
                    Icon(painter = painterResource(R.drawable.refresh_icon), contentDescription = "Refresh")
                }
            }
        }
        item {
            QuotationListComponent("Total Earning: ₹${formatNumberIntoIndianNumber(earningList.sumOf { it.totalEarning })}") {}
        }
        items (earningList){ earning->
            QuotationListComponent("+ ₹${formatNumberIntoIndianNumber(earning.totalEarning)}") {
                Router.navigateTo(Screen.EarningDetailScreen(earning))
            }
        }
    }
    SystemBackButtonHandler {
        Router.navigateTo(Screen.AdminSectionNavigatorScreen)
    }
}