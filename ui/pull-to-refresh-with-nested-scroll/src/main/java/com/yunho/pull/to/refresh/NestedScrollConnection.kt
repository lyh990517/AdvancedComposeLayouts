package com.yunho.pull.to.refresh

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun LazyListState.rememberNestedScrollConnectionWith(
    refreshState: RefreshState
): NestedScrollConnection {
    val scope = rememberCoroutineScope()

    return remember {
        object : NestedScrollConnection, CoroutineScope by scope {
            fun Float.toOffset(): Offset = Offset(0f, this)
            val atTop get() = !canScrollBackward

            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val scroll = available.y
                val headerHeight = layoutInfo.visibleItemsInfo.first().size
                val remainingScroll = headerHeight - scroll
                val consumed = minOf(-scroll, remainingScroll)

                return when {
                    firstVisibleItemIndex <= 1 -> {
                        if (atTop) {
                            launch { refreshState.snapTo(scroll) }
                        } else {
                            launch(Dispatchers.Main.immediate) { refreshState.snapTo(-abs(scroll)) }

                            if (refreshState.isPulling) return (-consumed).toOffset()
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
                launch { refreshState.refresh() }

                return super.onPostFling(consumed, available)
            }
        }
    }
}