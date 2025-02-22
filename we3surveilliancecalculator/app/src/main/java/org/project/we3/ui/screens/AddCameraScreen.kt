package org.project.we3.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AddCameraScreen(innerPaddingValues: PaddingValues, addCameraViewModel: AddCameraViewModel = viewModel()) {
    val context = LocalContext.current

    var cameraName by remember { mutableStateOf("") }
    var cameraDetails by remember { mutableStateOf("") }
    var mrp by remember { mutableStateOf("") }
    var unitPrice by remember { mutableStateOf("") }
    var gstRate by remember { mutableStateOf("18.0") }
    var quantity by remember { mutableStateOf("") }

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
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Text("Add New Camera", fontSize = 24.sp, color = Color.White, modifier = Modifier.align(Alignment.Start))
            Spacer(modifier = Modifier.height(20.dp))

            CameraStringInputField("Camera Name", cameraName) { cameraName = it }
            Spacing()
            CameraStringInputField("Camera Details", cameraDetails) { cameraDetails = it }
            Spacing()
            CameraStringInputField("MRP", mrp, KeyboardType.Number) { mrp = it }
            Spacing()
            CameraStringInputField(
                "Unit Price (Without GST)",
                unitPrice,
                KeyboardType.Number
            ) {
                unitPrice = it
            }
            Spacing()
            CameraStringInputField("GST Rate (%)", gstRate, KeyboardType.Number) { gstRate = it }
            Spacing()
            CameraStringInputField("Quantity (Stock)", quantity, KeyboardType.Number) {
                if(it.isDigitsOnly()) {
                    quantity = it
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
            if (addCameraViewModel.isLoading) {
                CircularProgressIndicator()
            } else {
                OutlinedButton(
                    onClick = {
                        addCameraViewModel.checkDataAndUpload(
                            context,
                            cameraName,
                            cameraDetails,
                            mrp,
                            unitPrice,
                            gstRate,
                            quantity
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp)
                        .clip(RoundedCornerShape(25.dp)),
                    border = BorderStroke(2.dp, Color.White),

                    ) {
                    Text("Add Camera", color = Color.White, fontSize = 22.sp)
                }
            }
        }
    }
}

@Composable
private fun Spacing() {
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun CameraStringInputField(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.White) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth(0.8f),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White,
            cursorColor = Color.White,
            unfocusedLabelColor = Color.White,
            focusedLabelColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            disabledLabelColor = Color.White,
            disabledTextColor = Color.White,
            disabledBorderColor = Color.White,
            disabledPlaceholderColor = Color.White,
            disabledPrefixColor = Color.White,
            disabledContainerColor = Color.White
        ),
        singleLine = true
    )
    Spacer(modifier = Modifier.height(10.dp))
}


