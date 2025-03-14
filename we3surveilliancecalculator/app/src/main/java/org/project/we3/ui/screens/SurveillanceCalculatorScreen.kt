package org.project.we3.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import org.project.we3.app.Camera
import org.project.we3.app.db.CameraViewModel

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun SurveillanceCalculatorScreen(
    cameraList: List<Camera>,
    innerPaddingValues: PaddingValues,
    cameraViewModel: CameraViewModel,
    surveillanceCalculatorViewModel: SurveillanceCalculatorViewModel = viewModel()
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val isLoading by cameraViewModel.isLoading.collectAsState()

    var showEmptyMessage by remember { mutableStateOf(false) }

    LaunchedEffect(cameraList) {
        if (cameraList.isEmpty()) {
            delay(500) // Delay before showing the message
            showEmptyMessage = true
        } else {
            showEmptyMessage = false
        }
    }
    when {
        isLoading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPaddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }
        showEmptyMessage -> {  // Prevents flashing by waiting for loading to finish
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPaddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "No cameras available. \nPlease check your internet connection.",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(20.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(onClick = { cameraViewModel.refreshCameras() }) {
                    Text("Retry", fontSize = 18.sp)
                }
            }
        }
        else -> {
            // Normal UI for when cameras are available
            Box(
                modifier = Modifier
                    .fillMaxSize()
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

                    CameraDropdownMenu(
                        cameraList = cameraList,
                        selectedCamera = surveillanceCalculatorViewModel.selectedCamera,
                        onCameraSelected = {
                            if (it != null) {
                                surveillanceCalculatorViewModel.selectCamera(it)
                            }
                        }
                    )
                    Spacing()
                    QuantityInputField(surveillanceCalculatorViewModel.quantity) {
                        surveillanceCalculatorViewModel.updateQuantity(it)
                    }
                    if (surveillanceCalculatorViewModel.quotation.camera.isNotEmpty()) {
                        Column (modifier = Modifier.fillMaxWidth(1f)
                            .heightIn(max = 120.dp)
                            .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                            Spacing()
                            surveillanceCalculatorViewModel.quotation.camera.forEachIndexed { index, camera ->
                                QuotationComponent(
                                    camera.name,
                                    surveillanceCalculatorViewModel.quotation.quantity[index]
                                ) {
                                    surveillanceCalculatorViewModel.removeCamera(index)
                                }
                            }
                            Spacing()
                        }
                    }
                    OutlinedButton(onClick = {
                        surveillanceCalculatorViewModel.addNewCameraButtonClicked(context)
                    }, modifier = Modifier.fillMaxWidth(0.8f)) {
                        Text("Add more cameras", fontSize = 18.sp)
                    }
                    Button(onClick = {
                        surveillanceCalculatorViewModel.continueButtonClicked(context)
                    }, modifier = Modifier.fillMaxWidth(0.8f)) {
                        Text("Continue", fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text("Protection You Can Trust.", color = Color.White, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
fun QuotationComponent(name: String, quantity: Int, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp, end=25.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("$name... ($quantity)", color = Color.White, fontSize = 16.sp, modifier = Modifier.weight(1f))
        IconButton(onClick = onRemove) {
            Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.Red)
        }
    }
}
