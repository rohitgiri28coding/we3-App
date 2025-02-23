package org.project.we3.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.project.we3.R

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    var scale by remember { mutableFloatStateOf(0f) }

    // Animate scale from 0.5x to 1.2x and then settle at 1x
    val animatedScale by animateFloatAsState(
        targetValue = if (scale == 0f) 1f else 3f,
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
        label = "Splash Animation"
    )

    // Start animation when the screen loads
    LaunchedEffect(Unit) {
        scale = 1f
        delay(2000) // Show splash screen for 2 seconds
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.app_logo), // Replace with your logo
                contentDescription = "App Logo",
                modifier = Modifier

                    .graphicsLayer(scaleX = animatedScale, scaleY = animatedScale) // Apply scaling animation
            )

            Spacer(modifier = Modifier.height(36.dp))

            Text(
                text = "We3 Surveillance Calculator",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
