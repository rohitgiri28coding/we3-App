package org.project.we3.ui.screens

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.project.we3.app.Camera
import org.project.we3.app.createQuotationPDF

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun MainScreen(cameraList: List<Camera?>?) {
    var selectedCamera by remember { mutableStateOf<Camera?>(null) }
    var quantity by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Camera Dropdown
        CameraDropdownMenu(
            cameraList = cameraList,
            onCameraSelected = { selectedCamera = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Quantity Input
        OutlinedTextField(
            value = quantity,
            label = { Text("Enter Quantity") },
            onValueChange = { input ->
                if (input.all { it.isDigit() }) {
                    quantity = input
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            isError = quantity.isNotEmpty() && quantity.toIntOrNull() == null,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Get Quotation Button
        Button(onClick = {
            if (selectedCamera == null) {
                Toast.makeText(context, "Please select a camera", Toast.LENGTH_SHORT).show()
            } else if (quantity.isEmpty() || quantity.toIntOrNull() == null || quantity.toInt() <= 0) {
                Toast.makeText(context, "Enter a valid quantity", Toast.LENGTH_SHORT).show()
            } else {
                if (quantity.toInt()>selectedCamera!!.quantity){
                    quantity = selectedCamera!!.quantity.toString()
                }
                val totalPrice = selectedCamera!!.unitPrice * quantity.toInt()
                Toast.makeText(context, "Quotation: ₹$totalPrice for $quantity", Toast.LENGTH_LONG).show()
                createQuotationPDF(context, listOf( selectedCamera!!), quantity.toInt())
            }
        }) {
            Text("Get Quotation")
        }
    }
}
fun getData(): StateFlow<List<Camera>> {
    val cameras = MutableStateFlow<List<Camera>>(emptyList())
    val db = FirebaseFirestore.getInstance()

    db.collection("CameraList")
        .orderBy("name")
        .addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("Firestore", "Error fetching camera: ${exception.message}")
                return@addSnapshotListener
            }
            val cameraList = snapshot?.toObjects(Camera::class.java) ?: emptyList()
            Log.d("Firestore", "Fetched ${cameraList.size} cameras")
            cameras.value = cameraList  // Update StateFlow
        }

    return cameras.asStateFlow() // Correct way to return StateFlow
}
fun removeDuplicates() {
    val db = FirebaseFirestore.getInstance()
    val cameraCollection = db.collection("CameraList")

    cameraCollection.get().addOnSuccessListener { snapshot ->
        if (snapshot != null && !snapshot.isEmpty) {
            val cameraMap = mutableMapOf<String, MutableList<DocumentSnapshot>>()

            // Group documents by a unique key (e.g., "name")
            for (document in snapshot.documents) {
                val cameraName = document.getString("name") ?: continue
                cameraMap.getOrPut(cameraName) { mutableListOf() }.add(document)
            }

            // Iterate over grouped cameras and delete duplicates
            for ((_, documents) in cameraMap) {
                if (documents.size > 1) {
                    // Keep the first document and delete the rest
                    val documentsToDelete = documents.drop(1)  // Drop first, keep the rest
                    for (doc in documentsToDelete) {
                        doc.reference.delete()
                            .addOnSuccessListener { Log.d("Firestore", "Deleted duplicate: ${doc.id}") }
                            .addOnFailureListener { e -> Log.e("Firestore", "Error deleting duplicate", e) }
                    }
                }
            }
        } else {
            Log.d("Firestore", "No data found")
        }
    }.addOnFailureListener { e ->
        Log.e("Firestore", "Error fetching documents", e)
    }
}

