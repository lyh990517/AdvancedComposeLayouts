package com.yunho.a3d_pages

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

internal sealed interface AnimationStep {
    val translationX: Float
    val rotationY: Float
    val scale: Float
    val next: AnimationStep?
    val previous: AnimationStep?
    val alpha: Float
    val farFromCenter: Int

    data object OffscreenLeft : AnimationStep {
        override val translationX: Float = -460f
        override val rotationY: Float = 5f
        override val scale: Float = 0.1f
        override val alpha: Float = 0f
        override val farFromCenter: Int = -3
        override val next: AnimationStep? = PeripheralLeft
        override val previous: AnimationStep? = null
    }

    data object PeripheralLeft : AnimationStep {
        override val translationX: Float = -380f
        override val rotationY: Float = 5f
        override val scale: Float = 0.25f
        override val alpha: Float = 1f
        override val farFromCenter: Int = -2
        override val next: AnimationStep? = PrimaryLeft
        override val previous: AnimationStep? = OffscreenLeft
    }

    data object PrimaryLeft : AnimationStep {
        override val translationX: Float = -200f
        override val rotationY: Float = 15f
        override val scale: Float = 0.5f
        override val alpha: Float = 1f
        override val farFromCenter: Int = -1
        override val next: AnimationStep? = Center
        override val previous: AnimationStep? = PeripheralLeft
    }

    data object Center : AnimationStep {
        override val translationX: Float = 100f
        override val rotationY: Float = 20f
        override val scale: Float = 0.8f
        override val alpha: Float = 1f
        override val farFromCenter: Int = 0
        override val next: AnimationStep? = PrimaryRight
        override val previous: AnimationStep? = PrimaryLeft
    }

    data object PrimaryRight : AnimationStep {
        override val translationX: Float = 500f
        override val rotationY: Float = 80f
        override val scale: Float = 1.3f
        override val alpha: Float = 1f
        override val farFromCenter: Int = 1
        override val next: AnimationStep? = OffscreenRight
        override val previous: AnimationStep? = Center
    }

    data object OffscreenRight : AnimationStep {
        override val translationX: Float = 700f
        override val rotationY: Float = 100f
        override val scale: Float = 1.4f
        override val alpha: Float = 0f
        override val farFromCenter: Int = 2
        override val next: AnimationStep? = null
        override val previous: AnimationStep? = PrimaryRight
    }

    companion object {
        private class AnimatedStep(
            override val translationX: Float,
            override val rotationY: Float,
            override val scale: Float,
            override val alpha: Float,
            override val farFromCenter: Int,
            override val next: AnimationStep?,
            override val previous: AnimationStep?
        ) : AnimationStep

        private fun MutableState<AnimationStep>.goToNext() {
            value = value.next ?: value
        }

        private fun MutableState<AnimationStep>.goToPrevious() {
            value = value.previous ?: value
        }

        fun Int.shouldCompose(center: Int): Boolean {
            val start = center + OffscreenLeft.farFromCenter
            val end = center + OffscreenRight.farFromCenter
            return this in start..end
        }

        fun List<MutableState<AnimationStep>>.flip(
            centerCursor: Int,
            direction: Direction,
        ): Int {
            val range = when (direction) {
                Direction.LEFT -> PeripheralLeft.farFromCenter..OffscreenRight.farFromCenter
                Direction.RIGHT -> OffscreenLeft.farFromCenter..PrimaryRight.farFromCenter
            }
            val start = centerCursor + range.first
            val end = centerCursor + range.last
            val shift = when (direction) {
                Direction.LEFT -> 1
                Direction.RIGHT -> -1
            }

            forEachIndexed { index, state ->
                if (index in start..end) {
                    when (direction) {
                        Direction.LEFT -> state.goToPrevious()
                        Direction.RIGHT -> state.goToNext()
                    }
                }
            }

            return centerCursor + shift
        }

        @Composable
        fun rememberSteps(items: List<*>) = remember(items) {
            val cursor = items.lastIndex

            buildList {
                items.indices.forEach { index ->
                    val firstStep = when (index) {
                        cursor -> Center
                        cursor - 1 -> PrimaryLeft
                        cursor - 2 -> PeripheralLeft
                        else -> OffscreenLeft
                    }

                    add(mutableStateOf(firstStep))
                }
            }
        }

        @Composable
        fun animateAsStep(
            targetStep: AnimationStep,
            animationSpec: FiniteAnimationSpec<Float> = tween(durationMillis = 700)
        ): AnimationStep {
            val translationX by animateFloatAsState(
                targetValue = targetStep.translationX,
                animationSpec = animationSpec
            )
            val rotationY by animateFloatAsState(
                targetValue = targetStep.rotationY,
                animationSpec = animationSpec
            )
            val scale by animateFloatAsState(
                targetValue = targetStep.scale,
                animationSpec = animationSpec
            )
            val alpha by animateFloatAsState(
                targetValue = targetStep.alpha,
                animationSpec = animationSpec
            )

            return AnimatedStep(
                translationX = translationX,
                rotationY = rotationY,
                scale = scale,
                alpha = alpha,
                farFromCenter = targetStep.farFromCenter,
                next = targetStep.next,
                previous = targetStep.previous
            )
        }
    }
}
