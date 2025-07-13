package com.yunho.pull.to.refresh

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yunho.common.Lottie
import com.yunho.common.R

@Composable
fun RefreshIndicator(
    refreshIndicator: RefreshIndicator
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(refreshIndicator.value),
        contentAlignment = Alignment.Center
    ) {
        Lottie(animId = R.raw.anim_loading)
    }
}