package com.yunho.pull.to.refresh

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.yunho.common.Lottie
import com.yunho.common.R

@Composable
fun RefreshIndicator(
    refreshIndicator: Animatable<Float, AnimationVector1D>
) {
    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(with(density) { refreshIndicator.value.toDp() }),
        contentAlignment = Alignment.Center
    ) {
        Lottie(animId = R.raw.anim_loading)
    }
}