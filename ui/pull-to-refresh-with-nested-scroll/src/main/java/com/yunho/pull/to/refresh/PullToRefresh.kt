package com.yunho.pull.to.refresh

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun PullToRefresh(
    samples: List<Painter>,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val parentListState = rememberLazyListState()
    val childListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val refreshIndicator = remember {
        Animatable(0f)
    }
    val maxOffset = with(density) { 90.dp.toPx() }
    val refreshOffset = with(density) { 60.dp.toPx() }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            fun Float.toOffset(): Offset = Offset(0f, this)

            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val scroll = available.y

                return when {
                    parentListState.firstVisibleItemIndex <= 1 && !childListState.canScrollBackward -> {
                        val headerHeight = parentListState.layoutInfo.visibleItemsInfo.first().size
                        val remainingScroll = headerHeight - scroll
                        val consumed = minOf(-scroll, remainingScroll)
                        if (!parentListState.canScrollBackward) {
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

                        parentListState.dispatchRawDelta(-scroll)
                        (-consumed).toOffset()
                    }

                    scroll > 0 && parentListState.canScrollBackward -> {
                        scope.launch {
                            parentListState.scrollBy(-scroll)
                        }
                        Offset(0f, scroll)
                    }

                    scroll < 0 && parentListState.canScrollForward -> {
                        scope.launch {
                            parentListState.scrollBy(-scroll)
                        }

                        Offset(0f, scroll)
                    }

                    else -> super.onPreScroll(available, source)
                }
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                scope.launch {
                    if (refreshIndicator.value > refreshOffset) {
                        launch {
                            refreshIndicator.animateTo(refreshOffset)
                        }

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

    LazyColumn(
        state = parentListState,
        modifier = modifier
    ) {
        item {
            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxWidth()
                    .height(60.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Topbar",
                    style = TextStyle(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }

        item {
            Text("Refresh")
        }

        item {
            LazyColumn(
                state = childListState,
                modifier = Modifier
                    .nestedScroll(nestedScrollConnection)
                    .fillMaxWidth()
                    .height(1500.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(samples) {
                    Image(
                        modifier = Modifier
                            .aspectRatio(2f / 3f)
                            .fillMaxWidth()
                            .padding(horizontal = 80.dp),
                        painter = it,
                        contentDescription = null
                    )
                }
            }
        }
    }
}