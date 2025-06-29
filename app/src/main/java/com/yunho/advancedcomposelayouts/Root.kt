package com.yunho.advancedcomposelayouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun Root() {
    val navController = LocalNavController.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { navController.navigateToAnimated3DPages() }
        ) {
            Text("Animated3DPages")
        }

        Button(
            onClick = { navController.navigateToMetallicShaderCard() }
        ) {
            Text("MetallicShaderCard")
        }

        Button(
            onClick = { navController.navigateToAnimatedGradient() }
        ) {
            Text("AnimatedGradient")
        }
    }
}
