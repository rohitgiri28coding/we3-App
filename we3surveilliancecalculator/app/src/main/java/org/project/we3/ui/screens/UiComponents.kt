package org.project.we3.ui.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.project.we3.R
import org.project.we3.app.Camera

@Composable
fun CameraImage(screenWidth: Dp) {

    val imageSize = if (screenWidth > 400.dp) 130.dp else 110.dp // Adjust image size dynamically

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth().background(Color.Transparent)
    ) {

        Image(
            painter = painterResource(id = R.drawable.c1),
            contentDescription = "Camera",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(imageSize)
                .clip(RoundedCornerShape(10.dp))
        )
            Image(
                painter = painterResource(id = R.drawable.c2),
                contentDescription = "Camera",
                modifier = Modifier
                    .size(imageSize)
                    .clip(RoundedCornerShape(10.dp))
            )
            Image(
                painter = painterResource(id = R.drawable.c3),
                contentDescription = "Camera",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(imageSize)
                    .clip(RoundedCornerShape(10.dp))
            )
    }
}

@Composable
fun CameraDropdownMenu(cameraList: List<Camera?>?, onCameraSelected: (Camera?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedCamera by remember { mutableStateOf<Camera?>(null) }

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        OutlinedTextField(
            value = selectedCamera?.name ?: "Select Camera",
            onValueChange = {},
            readOnly = true,
            label = { Text("Select Camera", color = Color.White) },
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Expand")
                }
            },
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                cursorColor = Color.White,
                unfocusedLabelColor = Color.White,
                focusedLabelColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledTextColor = Color.White,
                disabledBorderColor = Color.White,
                disabledLabelColor = Color.White,
                disabledTrailingIconColor = Color.White,

            ),
            textStyle = TextStyle(color = Color.White),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .clickable { expanded = !expanded }
        )
        DropdownMenu(
            modifier = Modifier.fillMaxWidth(0.7f).align(Alignment.Center),
            expanded = expanded,
            containerColor = Color.White,
            onDismissRequest = { expanded = false }
        ) {
            if (cameraList.isNullOrEmpty()|| cameraList.isEmpty()){
                DropdownMenuItem(
                    onClick = {},
                    text = { Text("No Camera Found", color = Color.White) }
                )
            }else {
                cameraList.forEachIndexed { index, camera ->
                    DropdownMenuItem(
                        onClick = {
                            selectedCamera = camera
                            onCameraSelected(camera)
                            expanded = false
                        },
                        text = { Text(camera!!.name, color = Color.Black) }
                    )
                    if (index < cameraList.size - 1) {
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp))
                    }
                }
            }

        }
    }

}


@Composable
fun QuantityInputField(screenWidth: Dp, updateQuantity: (String) -> Unit, onClick: () -> Unit) {
    var quantity by remember { mutableStateOf(TextFieldValue("")) }
    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        OutlinedTextField(
            value = quantity,
            onValueChange = {}, // Disabled manual input, handled via numpad
            placeholder = { Text("Enter Quantity", color=Color.White) },
            modifier = Modifier.fillMaxWidth(0.8f),
            readOnly = true, // Prevents manual typing
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                cursorColor = Color.White,
                unfocusedLabelColor = Color.White,
                focusedLabelColor = Color.White,
                disabledLabelColor = Color.White,
                disabledTextColor = Color.White,
                disabledBorderColor = Color.White,
                disabledPlaceholderColor = Color.White,
                disabledPrefixColor = Color.White,
                disabledContainerColor = Color.White
                ),
            textStyle = TextStyle(color = Color.White)
        )
        Spacer(modifier = Modifier.height(20.dp))

        NumberPad(screenWidth) { selectedNumber ->
            Log.d("Selected Number", selectedNumber)
            if (selectedNumber == "⌫") {
                if (quantity.text.isNotEmpty()) {
                    val newText = quantity.text.dropLast(1)
                    quantity = TextFieldValue(newText, selection = TextRange(newText.length)) // Cursor at end
                    updateQuantity(newText)
                }
            }else if (selectedNumber == "✔"){
                onClick.invoke()
            }
            else {
                val newText = quantity.text + selectedNumber
                quantity = TextFieldValue(newText, selection = TextRange(newText.length)) // Cursor at end
                updateQuantity(newText)
            }
        }
    }
}

@Composable
fun NumberPad(screenWidth: Dp, onNumberClick: (String) -> Unit) {
    val buttonSize = if (screenWidth > 400.dp) 80.dp else 70.dp // Adjust button size for larger screens

    val numbers = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("⌫", "0", "✔")
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        numbers.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { num ->
                    OutlinedButton(
                        onClick = { onNumberClick(num) },
                        modifier = Modifier
                            .size(buttonSize)  // Increased size
                            .padding(4.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,  // White button
                            contentColor = Color.Black // Black text
                        ),
                        border = BorderStroke(1.dp, Color.White)
                        ) {
                        Text(
                            text = num,
                            fontSize = if (screenWidth > 400.dp) 24.sp else 22.sp,  // Larger text on bigger screens
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

    }
}


