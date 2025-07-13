package com.yunho.pull.to.refresh

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yunho.common.Lottie
import com.yunho.common.R

@Composable
fun RefreshIndicator(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Lottie(
            modifier = Modifier.size(32.dp),
            animId = R.raw.anim_loading
        )
    }
}