package org.project.we3.app

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.shopping.we3surveillancecalculator.ui.theme.AppTheme
import org.project.we3.ui.screens.MainScreen

class MainActivity : ComponentActivity() {
    private val viewModel: CameraViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        viewModel.fetchFromFirestore()  // Fetch and save Firestore data in Room

        setContent {
            AppTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val cameraList by viewModel.cameras.collectAsState(initial = emptyList())

                    MainScreen(cameraList = cameraList)
                }
            }
        }
    }
}

