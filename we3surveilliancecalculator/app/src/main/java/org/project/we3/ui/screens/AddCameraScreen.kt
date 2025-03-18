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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.project.we3.app.db.CameraViewModel

@Composable
fun AddCameraScreen(innerPaddingValues: PaddingValues, cameraViewModel: CameraViewModel, addCameraViewModel: AddCameraViewModel = viewModel()) {
    val context = LocalContext.current

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

            CardEditSection("Camera Name", addCameraViewModel.cameraName, KeyboardType.Text){
                addCameraViewModel.updateCameraName(it)
            }
            Spacing()
            CardEditSection("Camera Details", addCameraViewModel.cameraDetails, KeyboardType.Text){
                addCameraViewModel.updateCameraDetails(it)
            }
            Spacing()
            CardEditSection("MRP", addCameraViewModel.mrp){
                addCameraViewModel.updateMRP(it)
            }
            Spacing()
            CardEditSection("Unit Price (Without GST)", addCameraViewModel.unitPrice){
                addCameraViewModel.updateUnitPrice(it)
            }
            Spacing()
            CardEditSection("GST Rate (%)", addCameraViewModel.gstRate){
                addCameraViewModel.updateGSTRate(it)
            }
            Spacing()
            CardEditSection("Quantity (Stock)", addCameraViewModel.quantity, imeAction = ImeAction.Done){
                addCameraViewModel.updateQuantity(it)
            }

            Spacer(modifier = Modifier.height(30.dp))
            if (addCameraViewModel.isLoading) {
                CircularProgressIndicator()
            } else {
                OutlinedButton(
                    onClick = {
                        addCameraViewModel.checkDataAndUpload(
                            context,
                            cameraViewModel
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



