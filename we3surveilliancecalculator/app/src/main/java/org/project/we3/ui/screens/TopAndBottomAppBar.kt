package org.project.we3.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.project.we3.app.navigation.AppScreenNavigation
import org.project.we3.app.navigation.Router
import org.project.we3.app.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
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
                            Router.navigateTo(Screen.AdminSectionNavigatorScreen)
                        }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Edit")
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