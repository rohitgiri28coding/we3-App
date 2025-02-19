package org.project.we3.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.project.we3.app.Camera

@Composable
fun CameraDropdownMenu(cameraList: List<Camera?>?, onCameraSelected: (Camera?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedCamera by remember { mutableStateOf<Camera?>(null) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedCamera?.name ?: "Select Camera",
            onValueChange = {},
            readOnly = true,
            label = { Text("Select Camera") },
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Expand")
                }
            },
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface, // Text color for disabled state
                disabledBorderColor = MaterialTheme.colorScheme.primary, // Border color
                disabledLabelColor = MaterialTheme.colorScheme.primary, // Label color
                disabledTrailingIconColor = MaterialTheme.colorScheme.primary, // Trailing icon color
                disabledPrefixColor = MaterialTheme.colorScheme.primary,
                disabledSuffixColor = MaterialTheme.colorScheme.primary,
                disabledPlaceholderColor = MaterialTheme.colorScheme.primary,
            ),
            modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (cameraList.isNullOrEmpty()){
                DropdownMenuItem(
                    onClick = {},
                    text = { Text("No Camera Found") }
                )
            }else {
                cameraList.forEachIndexed { index, camera ->
                    DropdownMenuItem(
                        onClick = {
                            selectedCamera = camera
                            onCameraSelected(camera)
                            expanded = false
                        },
                        text = { Text(camera!!.name) }
                    )
                    if (index < cameraList.size - 1) {
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp))
                    }
                }
            }

        }
    }

}
