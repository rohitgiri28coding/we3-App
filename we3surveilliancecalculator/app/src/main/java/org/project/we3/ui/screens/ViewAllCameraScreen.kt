package org.project.we3.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.project.we3.app.Camera

@Composable
fun ViewAllCameraScreen(innerPaddingValues: PaddingValues, viewAllCameraViewModel: ViewAllCameraViewModel = viewModel()) {
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
                    Spacer(modifier = Modifier.height(20.dp))

                    LazyColumn {
                        item {
                            AddCameraComponent{
                                viewAllCameraViewModel.navigateToAddCameraScreen()
                            }
                        }
                        items(viewAllCameraViewModel.cameras) { camera ->
                            CameraItem(camera) {
                                viewAllCameraViewModel.selectCameraAndNavigate(camera)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AddCameraComponent(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onClick()
            },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Row(horizontalArrangement = Arrangement.SpaceAround) {
            Icon(
                Icons.Filled.AddCircle,
                "Add Icon",
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "Add a new camera",
                modifier = Modifier.padding(16.dp),
                fontSize = 18.sp
            )
        }
    }
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

