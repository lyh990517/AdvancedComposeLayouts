package com.yunho.pull.to.refresh

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@Stable
class RefreshState(
    private val density: Density,
    private val maxOffset: Float,
    private val refreshOffset: Float,
    private val onRefresh: suspend RefreshState.() -> Unit
) {
    private val animatable = Animatable(0f)

    val isPulling: Boolean get() = animatable.value != 0f
    val value get() = with(density) { animatable.value.toDp() }

    suspend fun pull(value: Float) {
        animatable.snapTo((animatable.value + value).coerceIn(0f..maxOffset))
    }

    suspend fun rebound(value: Float) {
        animatable.snapTo((animatable.value - abs(value)).coerceIn(0f..maxOffset))
    }

    suspend fun refresh() {
        if (animatable.value > refreshOffset) {
            animatable.animateTo(refreshOffset)

            onRefresh()
        } else {
            reset()
        }
    }

    suspend fun reset() {
        animatable.animateTo(0f)
    }

    companion object {
        @Composable
        fun rememberRefreshState(
            refreshOffset: Dp = 60.dp,
            maxOffset: Dp = 90.dp,
            onRefresh: suspend RefreshState.() -> Unit
        ): RefreshState {
            val density = LocalDensity.current
            val maxOffset = with(density) { maxOffset.toPx() }
            val refreshOffset = with(density) { refreshOffset.toPx() }

            return remember {
                RefreshState(
                    density = density,
                    maxOffset = maxOffset,
                    refreshOffset = refreshOffset,
                    onRefresh = onRefresh
                )
            }
        }

        fun Modifier.onRefresh(refreshState: RefreshState) = height(
            refreshState.value
        )
    }
}