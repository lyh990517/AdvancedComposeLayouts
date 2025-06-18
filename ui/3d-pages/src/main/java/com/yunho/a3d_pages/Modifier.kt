package com.yunho.a3d_pages

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput

internal fun Modifier.setAnimation(
    itemsSize: Int,
    cursor: () -> Int,
    onCursorChanged: (Direction) -> Unit,
) = composed {
    var drag by remember { mutableFloatStateOf(0f) }

    pointerInput(Unit) {
        detectHorizontalDragGestures(
            onDragEnd = {
                val threshold = size.width / 4

                when {
                    drag > threshold -> if (cursor() > 0) onCursorChanged(Direction.RIGHT)
                    drag < -threshold -> if (cursor() < itemsSize) onCursorChanged(Direction.LEFT)
                }

                drag = 0f
            },
            onHorizontalDrag = { change, dragAmount ->
                drag += dragAmount
                change.consume()
            }
        )
    }
}

internal fun Modifier.animateByStep(step: AnimationStep) = graphicsLayer {
    alpha = step.alpha
    translationX = step.translationX
    rotationY = step.rotationY
    scaleX = step.scale
    scaleY = step.scale

    cameraDistance = 16 * density
}
