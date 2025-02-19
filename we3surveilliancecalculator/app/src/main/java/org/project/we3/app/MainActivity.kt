package org.project.we3.app

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.google.firebase.firestore.FirebaseFirestore
import com.shopping.we3surveillancecalculator.ui.theme.AppTheme
import org.project.we3.ui.screens.MainScreen
import org.project.we3.ui.screens.getData

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val cameraListFlow = getData()
                    val cameraList by cameraListFlow.collectAsState(initial = emptyList())

                    MainScreen(cameraList = cameraList)
                }
            }
        }
    }
}
fun uploadData(cameras: List<Camera>) {
    // Upload data to Firestore
    val db = FirebaseFirestore.getInstance()
    for (camera in cameras) {
        db.collection("CameraList")
            .add(
                hashMapOf(
                    "name" to camera.name,
                    "unitPrice" to camera.unitPrice,
                    "detail" to camera.detail,
                    "mrp" to camera.mrp,
                    "gst" to camera.gst,
                    "quantity" to camera.quantity
                )
            ).addOnSuccessListener {
                Log.d("MainActivity1", "DocumentSnapshot added with ID: $it")
            }.addOnFailureListener {
                Log.w("MainActivity1", "Error adding document", it)
            }
    }
}
