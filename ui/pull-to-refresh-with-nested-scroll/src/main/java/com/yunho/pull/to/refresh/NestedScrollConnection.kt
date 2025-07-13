package com.yunho.pull.to.refresh

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun LazyListState.rememberNestedScrollConnectionWith(
    refreshIndicator: Animatable<Float, AnimationVector1D>,
): NestedScrollConnection {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val maxOffset = with(density) { 90.dp.toPx() }
    val refreshOffset = with(density) { 60.dp.toPx() }

    return remember {
        object : NestedScrollConnection {
            fun Float.toOffset(): Offset = Offset(0f, this)

            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val scroll = available.y

                return when {
                    firstVisibleItemIndex <= 1 -> {
                        val headerHeight = layoutInfo.visibleItemsInfo.first().size
                        val remainingScroll = headerHeight - scroll
                        val consumed = minOf(-scroll, remainingScroll)
                        if (!canScrollBackward) {
                            scope.launch {
                                refreshIndicator.snapTo(
                                    (refreshIndicator.value + scroll).coerceAtMost(
                                        maxOffset
                                    )
                                )
                            }
                        } else {
                            scope.launch {
                                refreshIndicator.snapTo(
                                    (refreshIndicator.value - abs(scroll)).coerceAtLeast(
                                        0f
                                    )
                                )
                            }

                            if (refreshIndicator.value != 0f) {
                                return (-consumed).toOffset()
                            }
                        }

                        dispatchRawDelta(consumed)
                        if (source != NestedScrollSource.UserInput) {
                            Offset.Zero
                        } else {
                            (-consumed).toOffset()
                        }
                    }

                    else -> super.onPreScroll(available, source)
                }
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                scope.launch {
                    if (refreshIndicator.value > refreshOffset) {
                        refreshIndicator.animateTo(refreshOffset)

                        delay(2000)

                        refreshIndicator.animateTo(0f)
                    } else {
                        launch {
                            refreshIndicator.animateTo(0f)
                        }
                    }
                }

                return super.onPostFling(consumed, available)
            }
        }
    }
}