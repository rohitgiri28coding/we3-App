package org.project.we3.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.project.we3.app.Camera
import org.project.we3.app.db.Quotation
import org.project.we3.app.navigation.AppScreenNavigation
import org.project.we3.app.navigation.Router
import org.project.we3.app.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun TopAndBottomAppBar() {
    Scaffold(
        topBar = {
            Row (horizontalArrangement = Arrangement.Center){
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "We3 Surveillance Calculator",
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            HorizontalDivider(color = Color.White)
                        }
                    },
                    colors = TopAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent,
                        navigationIconContentColor = Color.White,
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White
                    )
                )
            }
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly){
                        IconButton(onClick = {
                            Router.navigateTo(Screen.HomeScreen)
                        }) {
                            Icon(Icons.Filled.Home, contentDescription = "Home")
                        }

                        IconButton(onClick = {
                            Router.navigateTo(Screen.ViewAllCameraScreen)
                        }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Edit")
                        }
                        IconButton(onClick = {
                            Router.navigateTo(Screen.ActiveQuotationListScreen(generateSampleQuotations()))
                        }) {
                            Icon(Icons.Default.Build, "")
                        }
                        IconButton(onClick = {
                            Router.navigateTo(Screen.ContactScreen)
                        }) {
                            Icon(Icons.Filled.Call, "Contact Us")
                        }
                    }

                },
                containerColor = Color.Transparent,
                contentColor = Color.Black,
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        AppScreenNavigation(innerPadding)
    }
}


fun generateSampleQuotations(): List<Quotation> {
    val sampleQuotations = mutableStateListOf<Quotation>()

    val camera1 = Camera(name = "Camera A", unitPrice = 100.0, detail = "...", mrp = 150.0, gst = 18.0, quantity = 1)
    val camera2 = Camera(name = "Camera B", unitPrice = 100.0, detail = "...", mrp = 150.0, gst = 18.0, quantity = 1)

    // Sample Quotation 1
    val cameras1 = listOf(camera2, camera1)
    val quantities1 = listOf(2, 1)
    val quotation1 = Quotation(
        camera = cameras1,
        quantity = quantities1,
        customerName = "John Doe",
        phoneNumber = "123-456-7890",
        dateGenerated = "2023-10-26",
        validity = "30 days",
        prepaymentAmount = 100.0,
        priority = true,
        extraDiscount = false,
        discountAmount = 0.0
    )
    sampleQuotations.add(quotation1)

    // Sample Quotation 2
    val cameras2 = listOf(camera2, camera1)
    val quantities2 = listOf(1, 3, 1)
    val quotation2 = Quotation(
        camera = cameras2,
        quantity = quantities2,
        customerName = "Jane Smith",
        phoneNumber = "987-654-3210",
        dateGenerated = "2023-10-27",
        validity = "15 days",
        prepaymentAmount = 50.0,
        priority = false,
        extraDiscount = true,
        discountAmount = 25.0
    )
    sampleQuotations.add(quotation2)

    // Sample Quotation 3
    val cameras3 = listOf(camera2, camera1)
    val quantities3 = listOf(4)
    val quotation3 = Quotation(
        camera = cameras3,
        quantity = quantities3,
        customerName = "David Lee",
        phoneNumber = "555-123-4567",
        dateGenerated = "2023-10-28",
        validity = "60 days",
        prepaymentAmount = 200.0,
        priority = true,
        extraDiscount = true,
        discountAmount = 50.0
    )
    sampleQuotations.add(quotation3)

    // Sample Quotation 4
    val cameras4 = listOf(camera2, camera1)
    val quantities4 = listOf(2, 2)
    val quotation4 = Quotation(
        camera = cameras4,
        quantity = quantities4,
        customerName = "Emily White",
        phoneNumber = "111-222-3333",
        dateGenerated = "2023-10-29",
        validity = "45 days",
        prepaymentAmount = 75.0,
        priority = false,
        extraDiscount = false,
        discountAmount = 0.0
    )
    sampleQuotations.add(quotation4)

    // Sample Quotation 5
    val cameras5 = listOf(camera2, camera1)
    val quantities5 = listOf(1, 1, 1)
    val quotation5 = Quotation(
        camera = cameras5,
        quantity = quantities5,
        customerName = "Michael Brown",
        phoneNumber = "444-555-6666",
        dateGenerated = "2023-10-30",
        validity = "30 days",
        prepaymentAmount = 150.0,
        priority = true,
        extraDiscount = true,
        discountAmount = 30.0
    )
    sampleQuotations.add(quotation5)

    return sampleQuotations
}