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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDateRangePickerState
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.project.we3.R
import org.project.we3.app.Camera
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerModal(
    startDate: Long?,
    endDate: Long?,
    onDateRangeSelected: (Pair<String?, String?>) -> Unit, // Changed to String?
    onDismiss: () -> Unit
) {
    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = startDate,
        initialSelectedEndDateMillis = endDate
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val startDateMillis = dateRangePickerState.selectedStartDateMillis
                    val endDateMillis = dateRangePickerState.selectedEndDateMillis

                    val startDateString = startDateMillis?.let {
                        Instant.ofEpochMilli(it)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                    }

                    val endDateString = endDateMillis?.let {
                        Instant.ofEpochMilli(it)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                    }
                    onDateRangeSelected(
                        Pair(
                            startDateString,
                            endDateString
                        )
                    )
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            title = {
                Text(
                    text = "Select Quotation expiryDate range",
                    fontSize = 14.sp
                )
            },
            showModeToggle = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 10.dp, start = 8.dp, end = 5.dp)
        )
    }
}

@Composable
fun CustomAlertDialogBox(title: String, message: String, confirmButtonText: String, confirmButtonClicked: (Boolean)-> Unit, onDismiss: (Boolean) -> Unit){
    AlertDialog(
        onDismissRequest = { onDismiss(false) },
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            Button(
                onClick = {
                    confirmButtonClicked(false)
                },
                colors = ButtonDefaults.buttonColors(Color.Red)
            ) {
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            Button(onClick = {onDismiss(false)}) {
                Text("Cancel")
            }
        }
    )
}


@Composable
fun QuotationListComponent(data: String, onClick: () -> Unit){
    Column(modifier = Modifier.padding(10.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth().clickable{onClick.invoke()},
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text(text = data, modifier = Modifier.padding(16.dp), fontSize = 18.sp)
        }
        Spacing(5.dp)
    }
}

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
fun CameraDropdownMenu(cameraList: List<Camera>, selectedCamera: Camera?, onCameraSelected: (Camera?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier,
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
            if (cameraList.isEmpty()){
                Log.d("CameraDropdownMenu", "No Camera Found")
                DropdownMenuItem(
                    onClick = {

                    },
                    text = { Text("No Camera Found.", color = Color.Black) }
                )
            }else {
                cameraList.forEachIndexed { index, camera ->
                    DropdownMenuItem(
                        onClick = {
                            onCameraSelected(camera)
                            expanded = false
                        },
                        text = { Text(camera.name, color = Color.Black) }
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
fun CheckComponent(text: String, checked: Boolean, updateCheckValue: (Boolean) -> Unit){

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text,
            color = Color.White
        )
        Checkbox(
            checked = checked,
            onCheckedChange = { updateCheckValue(it) }
        )
    }
}

@Composable
fun InputField(text: String, txtValue: String, keyboardType: KeyboardType= KeyboardType.Number, imeAction: ImeAction= ImeAction.Next, updateText: (String)-> Unit){
    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = txtValue,
            onValueChange = {
                updateText(it)
            },
            placeholder = { Text("Enter $text", color = Color.White) },
            modifier = Modifier.fillMaxWidth(0.8f),
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
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType, imeAction = imeAction),
            textStyle = TextStyle(color = Color.White)
        )
        Spacing(15.dp)
    }
}
@Composable
fun Spacing(size: Dp = 20.dp){
    Spacer(modifier = Modifier.size(size))
}

@Composable
fun CardEditSection(textValue: String, value: String, keyboardType: KeyboardType = KeyboardType.Number, imeAction: ImeAction = ImeAction.Next, onTextChange: (String)-> Unit){
    Column (modifier = Modifier.padding(5.dp)){
        Text(textValue, color = Color.Black, fontSize = 17.sp, modifier = Modifier.padding(5.dp), fontWeight = FontWeight.Bold)
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            TextField(
                value = value,
                onValueChange = { onTextChange(it) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType, imeAction = imeAction),
                modifier = Modifier.fillMaxSize(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )
        }
    }
}
@Composable
fun QuantityInputField(quantity: String, updateQuantity: (String) -> Unit) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        OutlinedTextField(
            value = quantity,
            onValueChange = {
                updateQuantity(it)
            },
            placeholder = { Text("Enter Quantity", color=Color.White) },
            modifier = Modifier.fillMaxWidth(0.8f),
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
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            textStyle = TextStyle(color = Color.White)
        )
        Spacer(modifier = Modifier.height(20.dp))


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


