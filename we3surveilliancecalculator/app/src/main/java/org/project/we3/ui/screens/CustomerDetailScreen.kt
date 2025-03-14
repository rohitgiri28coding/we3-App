package org.project.we3.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.project.we3.app.db.Quotation

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun CustomerDetailScreen(
    innerPaddingValues: PaddingValues = PaddingValues(),
    quotation: Quotation,
    customerDetailViewModel: CustomerDetailViewModel = viewModel()
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val context = LocalContext.current

    LaunchedEffect(quotation) {
        customerDetailViewModel.setQuotation(quotation)
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

            InputField("Prepayment Amount", customerDetailViewModel.prepayment) {
                customerDetailViewModel.updatePrepayment(it)
            }

            if(customerDetailViewModel.extraDiscount){
                InputField("Discount Amount", customerDetailViewModel.discountAmount){
                    customerDetailViewModel.updateDiscountAmount(it)
                }
            }
            Button(onClick = {
                customerDetailViewModel.showDatePicker = true
            }) {
                Text("Select Date Range")
            }

            if (customerDetailViewModel.showDatePicker) {
                DateRangePickerModal(
                    onDateRangeSelected = { dates ->
                        customerDetailViewModel.updateQuotationDateRange(dates.first, dates.second)
                        customerDetailViewModel.showDatePicker = false
                    },
                    onDismiss = { customerDetailViewModel.showDatePicker = false }
                )
            }
            CheckComponent(
                "Extra Discount",
                checked = customerDetailViewModel.extraDiscount,
                updateCheckValue = {
                    customerDetailViewModel.updateExtraDiscount(it)
                },
            )   //

            CheckComponent(
                "Priority",
                checked = customerDetailViewModel.priority,
                updateCheckValue = {
                    customerDetailViewModel.updatePriority(it)
                }
            )


            Button(onClick = {
                customerDetailViewModel.generateAndSaveQuotation(context)
            }) {
                Text("Generate Quotation")
            }

        }
    }
}


