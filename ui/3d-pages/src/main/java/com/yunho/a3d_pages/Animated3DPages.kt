package com.yunho.a3d_pages

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yunho.a3d_pages.AnimationStep.Companion.animateAsStep
import com.yunho.a3d_pages.AnimationStep.Companion.flip
import com.yunho.a3d_pages.AnimationStep.Companion.rememberSteps
import com.yunho.a3d_pages.AnimationStep.Companion.shouldCompose

@Composable
fun <T> Animated3DPages(
    items: List<T>,
    modifier: Modifier = Modifier,
    itemContent: @Composable (T) -> Unit = {}
) {
    val steps = rememberSteps(items)
    var centerCursor by remember { mutableIntStateOf(steps.lastIndex) }

    Box(
        modifier = modifier
            .setAnimation(
                itemsSize = items.lastIndex,
                cursor = { centerCursor },
                onCursorChanged = { direction ->
                    centerCursor = when (direction) {
                        Direction.LEFT -> steps.flip(centerCursor = centerCursor, direction = direction)
                        Direction.RIGHT -> steps.flip(centerCursor = centerCursor, direction = direction)
                    }
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        items.forEachIndexed { index, item ->
            val step by remember { steps[index] }

            key(index) {
                if (index.shouldCompose(centerCursor)) {
                    Animated3DPage(
                        step = step,
                        item = item,
                        itemContent = itemContent
                    )
                }
            }
        }
    }
}

@Composable
private fun <T> Animated3DPage(
    item: T,
    step: AnimationStep,
    itemContent: @Composable (T) -> Unit = {}
) {
    val animatedStep = animateAsStep(step)

    Box(
        modifier = Modifier.animateByStep(animatedStep)
    ) {
        itemContent(item)
    }
}
