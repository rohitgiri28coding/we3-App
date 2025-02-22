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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.lifecycle.viewmodel.compose.viewModel
import org.project.we3.app.db.CameraViewModel

@Composable
fun AddCameraScreen(innerPaddingValues: PaddingValues, cameraViewModel: CameraViewModel, addCameraViewModel: AddCameraViewModel = viewModel()) {
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

            Text("Add New Camera", fontSize = 24.sp, color = Color.Black, modifier = Modifier.align(Alignment.Start))
            Spacer(modifier = Modifier.height(20.dp))

            cameraName = cameraEditSection("Camera Name", cameraName, KeyboardType.Text)
            Spacing()
            cameraDetails = cameraEditSection("Camera Details", cameraDetails, KeyboardType.Text)
            Spacing()
            mrp = cameraEditSection("MRP", mrp)
            Spacing()
            unitPrice = cameraEditSection("Unit Price (Without GST)", unitPrice)
            Spacing()
            gstRate = cameraEditSection("GST Rate (%)", gstRate)
            Spacing()
            quantity = cameraEditSection("Quantity (Stock)", quantity)

            Spacer(modifier = Modifier.height(30.dp))
            if (addCameraViewModel.isLoading) {
                CircularProgressIndicator()
            } else {
                OutlinedButton(
                    onClick = {
                        addCameraViewModel.checkDataAndUpload(
                            context,
                            cameraViewModel,
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
    Spacer(modifier = Modifier.height(20.dp))
}


@Composable
private fun editSection(textValue: String, value: String, keyboardType: KeyboardType = KeyboardType.Number): String {
    var v1 by remember {  mutableStateOf(value)}
    TextField(
        value = v1,
        onValueChange = { v1 = it },
        label = { Text(textValue, color = Color.Black) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedTextColor = Color.Black, unfocusedTextColor = Color.Black)
    )
    return v1
}

@Composable
private fun cameraEditSection(textValue: String, value: String, keyboardType: KeyboardType = KeyboardType.Number): String {
    var v1 by remember {  mutableStateOf(value)}
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        v1 = editSection(textValue, v1, keyboardType)
    }
    return v1
}

