package com.yunho.animated_gradient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedGradient(
    modifier: Modifier = Modifier
) {
    val animatedGradient by rememberAnimatedGradient()

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .aspectRatio(2 / 3f)
                .clip(RoundedCornerShape(20.dp))
                .background(brush = Brush.linearGradient(animatedGradient))
        )
    }
}