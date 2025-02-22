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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.project.we3.app.Camera

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun SurveillanceCalculatorScreen(
    cameraList: List<Camera?>?,
    innerPaddingValues: PaddingValues,
    surveillanceCalculatorViewModel: SurveillanceCalculatorViewModel = viewModel()
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

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
                onCameraSelected = {
                    if (it != null) {
                        surveillanceCalculatorViewModel.selectCamera(it)
                    }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            QuantityInputField(screenWidth, {
                surveillanceCalculatorViewModel.updateQuantity(it)
            }, {
                surveillanceCalculatorViewModel.validateAndGenerateQuotation(context)
            })

            Spacer(modifier = Modifier.weight(1f))
            Text("Protection You Can Trust.", color = Color.White, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
