package org.project.we3.ui.screens.admin.camera

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.project.we3.R
import org.project.we3.data.model.Camera
import org.project.we3.domain.repository.CameraViewModel
import org.project.we3.ui.navigation.Router
import org.project.we3.ui.navigation.Screen
import org.project.we3.ui.navigation.SystemBackButtonHandler

@Composable
fun ViewAllCameraScreen(cameraList: List<Camera>, innerPaddingValues: PaddingValues, cameraViewModel: CameraViewModel, viewAllCameraViewModel: ViewAllCameraViewModel = viewModel ()) {
    LaunchedEffect(Unit) {
        cameraViewModel.refreshCameras()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            if (viewAllCameraViewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Camera List",
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
                    items(cameraList) { camera ->
                        CameraItem(camera.name) {
                            viewAllCameraViewModel.navigateToCameraDetailScreen(camera)
                        }
                    }
                }

            }
        }
    }
    SystemBackButtonHandler { Router.navigateTo(Screen.AdminSectionNavigatorScreen) }
}
@Composable
fun CameraItem(cameraName: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp).clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Text(text = cameraName, modifier = Modifier.padding(16.dp), fontSize = 18.sp)
    }
}

