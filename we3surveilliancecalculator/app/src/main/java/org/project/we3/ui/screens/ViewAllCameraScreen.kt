package org.project.we3.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.project.we3.app.Camera
import org.project.we3.app.navigation.Router
import org.project.we3.app.navigation.Screen
import org.project.we3.app.navigation.SystemBackButtonHandler

@Composable
fun ViewAllCameraScreen(cameraList: List<Camera>, innerPaddingValues: PaddingValues, viewAllCameraViewModel: ViewAllCameraViewModel = viewModel ()) {
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
                Column {
                    LazyColumn {
                        item{
                            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                                Text("Camera List", modifier = Modifier.padding(16.dp), fontSize = 24.sp)
                            }
                        }
                        items(cameraList) { camera ->
                            CameraItem(camera) {
                                viewAllCameraViewModel.navigateToCameraDetailScreen(camera)
                            }
                        }
                    }
                }
            }
        }
    }
    SystemBackButtonHandler { Router.navigateTo(Screen.AdminSectionNavigatorScreen) }
}
@Composable
private fun CameraItem(camera: Camera, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp).clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Text(text = camera.name, modifier = Modifier.padding(16.dp), fontSize = 18.sp)
    }
}

