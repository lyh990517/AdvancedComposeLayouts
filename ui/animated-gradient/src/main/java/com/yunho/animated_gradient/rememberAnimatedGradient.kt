package com.yunho.animated_gradient

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import kotlin.math.floor

@Composable
fun rememberAnimatedGradient(
    gradientColors: List<Color> = listOf(
        Color(0xFF6731FF),
        Color(0xFF8000FF),
        Color(0xFF00FFFF),
    ),
    alpha: Float = 1f,
    duration: Int = 2000,
): State<List<Color>> {
    val totalDuration = duration * gradientColors.size
    val transition = rememberInfiniteTransition(label = "gradient")
    val gradientAnimationProgress by transition.animateFloat(
        initialValue = gradientColors.size.toFloat(),
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(totalDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "gradientProgress"
    )

    return remember(gradientAnimationProgress, alpha) {
        derivedStateOf {
            val startIndex = floor(gradientAnimationProgress).toInt() % gradientColors.size
            val fraction = gradientAnimationProgress - floor(gradientAnimationProgress)

            List(3) { offset ->
                val from =
                    gradientColors[(startIndex + offset) % gradientColors.size].copy(alpha = alpha)
                val to =
                    gradientColors[(startIndex + offset + 1) % gradientColors.size].copy(alpha = alpha)
                lerp(from, to, fraction)
            }
        }
    }
}

