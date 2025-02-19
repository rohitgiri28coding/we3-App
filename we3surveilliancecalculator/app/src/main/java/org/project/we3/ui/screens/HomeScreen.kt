package org.project.we3.ui.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.project.we3.app.Camera
import org.project.we3.app.createQuotationPDF

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun MainScreen(cameraList: List<Camera?>?) {
    var selectedCamera by remember { mutableStateOf<Camera?>(null) }
    var quantity by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Camera Dropdown
        CameraDropdownMenu(
            cameraList = cameraList,
            onCameraSelected = { selectedCamera = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Quantity Input
        OutlinedTextField(
            value = quantity,
            label = { Text("Enter Quantity") },
            onValueChange = { input ->
                if (input.all { it.isDigit() }) {
                    quantity = input
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            isError = quantity.isNotEmpty() && quantity.toIntOrNull() == null,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Get Quotation Button
        Button(onClick = {
            if (selectedCamera == null) {
                Toast.makeText(context, "Please select a camera", Toast.LENGTH_SHORT).show()
            } else if (quantity.isEmpty() || quantity.toIntOrNull() == null || quantity.toInt() <= 0) {
                Toast.makeText(context, "Enter a valid quantity", Toast.LENGTH_SHORT).show()
            } else {
                if (quantity.toInt()>selectedCamera!!.quantity){
                    quantity = selectedCamera!!.quantity.toString()
                }
                val totalPrice = selectedCamera!!.unitPrice * quantity.toInt()
                Toast.makeText(context, "Quotation: ₹$totalPrice for $quantity", Toast.LENGTH_LONG).show()
                createQuotationPDF(context, listOf( selectedCamera!!), quantity.toInt())
            }
        }) {
            Text("Get Quotation")
        }
    }
}


