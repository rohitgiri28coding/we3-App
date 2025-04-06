package org.project.we3.ui.screens.calculator

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import org.project.we3.data.model.Quotation
import org.project.we3.ui.navigation.Router
import org.project.we3.ui.navigation.Screen
import org.project.we3.ui.navigation.SystemBackButtonHandler
import org.project.we3.ui.screens.CameraImage
import org.project.we3.ui.screens.CheckComponent
import org.project.we3.ui.screens.DateRangePickerModal
import org.project.we3.ui.screens.InputField
import org.project.we3.ui.screens.Spacing

@Composable
fun CustomerDetailScreen(
    innerPaddingValues: PaddingValues = PaddingValues(),
    quotation: Quotation,
    customerDetailViewModel: CustomerDetailViewModel = hiltViewModel()
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val context = LocalContext.current

    LaunchedEffect(quotation) {
        customerDetailViewModel.updateQuotation(quotation)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(innerPaddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            CameraImage(screenWidth)

            Spacer(modifier = Modifier.height(10.dp))

            InputField("Customer Name", customerDetailViewModel.customerName, KeyboardType.Text) {
                customerDetailViewModel.updateCustomerName(it)
            }

            InputField("Phone Number", customerDetailViewModel.phoneNumber) {
                customerDetailViewModel.updatePhoneNumber(it)
            }

            InputField(
                "Prepayment Amount",
                customerDetailViewModel.prepayment,
                imeAction = if (customerDetailViewModel.extraDiscount) ImeAction.Next else ImeAction.Done
            ) {
                customerDetailViewModel.updatePrepayment(it)
            }

            if(customerDetailViewModel.extraDiscount){
                InputField(
                    "Discount Amount",
                    customerDetailViewModel.discountAmount,
                    imeAction = ImeAction.Done
                ) {
                    customerDetailViewModel.updateDiscountAmount(it)
                }
            }
            OutlinedButton(onClick = {
                customerDetailViewModel.showDatePicker = true
            }, border = BorderStroke(1.dp, Color.White), modifier = Modifier.fillMaxWidth(0.8f)) {
                Text("Select Date Range", color = Color.White, fontSize = 16.sp)
            }

            if (customerDetailViewModel.showDatePicker) {
                DateRangePickerModal(
                    startDate = customerDetailViewModel.currentMillis,
                    endDate = customerDetailViewModel.thirtyDaysLaterMillis,
                    onDateRangeSelected = { dates ->
                        customerDetailViewModel.updateQuotationDateRange(
                            dates.first,
                            dates.second
                        )
                        customerDetailViewModel.showDatePicker = false
                    },
                    onDismiss = { customerDetailViewModel.showDatePicker = false }
                )
            }
            Row (modifier = Modifier.fillMaxWidth(0.8f), horizontalArrangement = Arrangement.SpaceBetween){
                CheckComponent(
                    "Extra Discount",
                    checked = customerDetailViewModel.extraDiscount,
                    updateCheckValue = {
                        customerDetailViewModel.updateExtraDiscount(it)
                    },
                )

                CheckComponent(
                    "Priority",
                    checked = customerDetailViewModel.priority,
                    updateCheckValue = {
                        customerDetailViewModel.updatePriority(it)
                    }
                )
            }

            Spacing(40.dp)

            Button(onClick = {
                customerDetailViewModel.generateAndSaveQuotation(context)
            }, modifier = Modifier.fillMaxWidth(0.8f).height(40.dp)) {
                Text("Generate Quotation", fontSize = 20.sp)
            }

        }
        SystemBackButtonHandler {
            Router.navigateTo(Screen.HomeScreen)
        }
    }
}
