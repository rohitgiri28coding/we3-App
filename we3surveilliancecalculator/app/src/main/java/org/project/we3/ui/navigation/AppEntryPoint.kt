package org.project.we3.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.project.we3.ui.screens.SplashScreen
import org.project.we3.ui.screens.TopAndBottomAppBar

@Composable
fun AppEntryPoint() {
    var showSplash by remember { mutableStateOf(true) }

    if (showSplash) {
        SplashScreen { showSplash = false }
    } else {
        TopAndBottomAppBar()
    }
}

