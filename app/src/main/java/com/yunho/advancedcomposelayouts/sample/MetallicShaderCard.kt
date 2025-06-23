package com.yunho.advancedcomposelayouts.sample

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.yunho.metallic_shader_card.MetallicShaderCard
import com.yunho.metallic_shader_card.R

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MetallicShaderCardSample() {
    MetallicShaderCard(
        id = R.drawable.yunho_lee_business_card,
        modifier = Modifier
            .height(350.dp)
            .aspectRatio(1 / 1.58f)
            .clip(RoundedCornerShape(20.dp))
    )
}
