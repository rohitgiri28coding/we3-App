package org.project.we3.ui.screens

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ContactUsScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(26.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Contact Us", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(20.dp))

        ContactButton(
            icon = Icons.Default.Call,
            label = "Call Us",
            onClick = { openDialer(context, "+91 9386673993") }
        )

        ContactButton(
            icon = Icons.Default.Email,
            label = "Email Us",
            onClick = { openEmail(context, "we3support@gmail.com") }
        )

        ContactButton(
            icon = Icons.Default.LocationOn,
            label = "Find Us",
            onClick = { openMaps(context, "25.618681", "85.150139") }        )
    }
}

@Composable
fun ContactButton(icon: ImageVector, label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp)
    ) {
        Icon(imageVector = icon, contentDescription = label, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(30.dp))
        Text(text = label, fontSize = 22.sp)
    }
}

fun openDialer(context: Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
    context.startActivity(intent)
}

fun openEmail(context: Context, email: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:$email")
    }
    context.startActivity(intent)
}


fun openMaps(context: Context, latitude: String, longitude: String) {
    val location = "$latitude,$longitude"
    val uri = Uri.parse("geo:$location?q=$location")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage("com.google.android.apps.maps")
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        val mapUrl = "https://maps.google.com/?q=$location"
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl))
        context.startActivity(webIntent)
    }
}

