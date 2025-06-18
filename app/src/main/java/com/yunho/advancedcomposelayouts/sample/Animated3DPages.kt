package com.yunho.advancedcomposelayouts.sample

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.yunho.a3d_pages.Animated3DPages

@Composable
fun Animated3DPagesSample() {
    Animated3DPages(samples) { page ->
        Image(
            painter = painterResource(page),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3 / 5f)
                .clip(RoundedCornerShape(20.dp))
        )
    }
}
