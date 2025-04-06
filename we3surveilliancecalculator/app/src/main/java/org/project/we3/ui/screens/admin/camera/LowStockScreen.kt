package org.project.we3.ui.screens.admin.camera

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import org.project.we3.data.model.Camera
import org.project.we3.domain.repository.CameraViewModel
import org.project.we3.ui.navigation.Router
import org.project.we3.ui.navigation.Screen
import org.project.we3.ui.navigation.SystemBackButtonHandler

@Composable
fun LowStockScreen(cameraList: List<Camera>, cameraViewModel:CameraViewModel, innerPaddingValues: PaddingValues, lowStockViewModel: LowStockViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        lowStockViewModel.addLowStockCamera(cameraList)
    }
    val lowStockCameras = lowStockViewModel.lowStockCameras.collectAsState(initial = emptyList()).value
    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        LazyColumn(modifier = Modifier.padding(innerPaddingValues)) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Low Stock",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 24.sp
                    )
                    IconButton(onClick = { cameraViewModel.refreshCameras() }) {
                        Icon(
                            painter = painterResource(R.drawable.refresh_icon),
                            contentDescription = "Refresh"
                        )
                    }
                }
            }

            items(lowStockCameras) { camera ->
                CameraItem("${camera.name} (${camera.quantity})") {
                    Router.navigateTo(Screen.CameraDetailScreen(camera))
                }
            }
        }
    }
    SystemBackButtonHandler {
        Router.navigateTo(Screen.AdminSectionNavigatorScreen)
    }
}