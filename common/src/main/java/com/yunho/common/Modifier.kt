package com.yunho.common

import androidx.compose.animation.core.animate
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import kotlinx.coroutines.launch

fun Modifier.pinchZoom(resetOnRelease: Boolean = true) = composed {
    val scope = rememberCoroutineScope()
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(TransformOrigin(0.5f, 0.5f)) }
    var baseOffset by remember { mutableStateOf(TransformOrigin(0.5f, 0.5f)) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }

    graphicsLayer {
        transformOrigin = offset
        scaleX = scale
        scaleY = scale
        clip = true
    }.pointerInput(Unit) {
        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent()
                val changes = event.changes

                if (changes.size >= 2) {
                    val firstPointer = changes[0]
                    val secondPointer = changes[1]

                    val firstCurrent = firstPointer.position
                    val secondCurrent = secondPointer.position
                    val firstPrevious = firstPointer.previousPosition
                    val secondPrevious = secondPointer.previousPosition

                    val currentDistance = (firstCurrent - secondCurrent).getDistance()
                    val previousDistance = (firstPrevious - secondPrevious).getDistance()

                    if (previousDistance > 0) {
                        val zoomChange = currentDistance / previousDistance
                        val newScale = (scale * zoomChange).coerceIn(1f, 5f)

                        if (newScale != scale || scale > 1f) {
                            scale = newScale

                            if (scale > 1f) {
                                val centroid = event.calculateCentroid(useCurrent = false)

                                val newOffset = TransformOrigin(
                                    centroid.x / size.width,
                                    centroid.y / size.height
                                )

                                if (event.type != PointerEventType.Release) {
                                    offset = newOffset
                                    baseOffset = newOffset
                                    dragOffset = Offset.Zero
                                }
                            }

                            changes.forEach { it.consume() }
                        }
                    }
                } else if (changes.size == 1 && scale > 1f) {
                    val change = changes[0]
                    val dragAmount = change.positionChange()
                    val newDragOffset = dragOffset - dragAmount

                    val scaledWidth = size.width * scale
                    val scaledHeight = size.height * scale

                    val maxOffsetX = (scaledWidth - size.width) / 2f
                    val maxOffsetY = (scaledHeight - size.height) / 2f

                    val baseX = baseOffset.pivotFractionX * size.width
                    val baseY = baseOffset.pivotFractionY * size.height

                    val limitedDragOffsetX = newDragOffset.x.coerceIn(
                        -(baseX + maxOffsetX - size.width / 2f),
                        maxOffsetX + size.width / 2f - baseX
                    )
                    val limitedDragOffsetY = newDragOffset.y.coerceIn(
                        -(baseY + maxOffsetY - size.height / 2f),
                        maxOffsetY + size.height / 2f - baseY
                    )

                    dragOffset = Offset(limitedDragOffsetX, limitedDragOffsetY)

                    val x = baseX + dragOffset.x
                    val y = baseY + dragOffset.y

                    offset = TransformOrigin(
                        (x / size.width).coerceIn(0f, 1f),
                        (y / size.height).coerceIn(0f, 1f)
                    )

                    change.consume()
                }

                if (changes.all { !it.pressed } && scale > 1f && resetOnRelease) {
                    scope.launch {
                        animate(
                            initialValue = scale,
                            targetValue = 1f
                        ) { value, _ -> scale = value }

                        offset = TransformOrigin(0.5f, 0.5f)
                        baseOffset = offset
                        dragOffset = Offset.Zero
                    }
                }
            }
        }
    }
}
