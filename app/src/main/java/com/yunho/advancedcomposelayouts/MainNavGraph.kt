package com.yunho.advancedcomposelayouts

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yunho.a3d_pages.Animated3DPages
import com.yunho.animated_gradient.AnimatedGradient
import com.yunho.common.pinchZoom
import com.yunho.common.samples
import com.yunho.metallic_shader_card.MetallicShaderCard
import com.yunho.metallic_shader_card.R
import com.yunho.pull.to.refresh.PullToRefresh

val LocalNavController = compositionLocalOf<NavController> {
    error("LocalNavController")
}

@Composable
fun MainNavGraph(
    navController: NavHostController = rememberNavController()
) {
    CompositionLocalProvider(
        LocalNavController provides navController
    ) {
        NavHost(
            modifier = Modifier.fillMaxSize(),
            navController = navController,
            startDestination = Root
        ) {
            composable<Root> {
                Root()
            }

            composable<Animated3DPages> {
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

            composable<MetallicShaderCard> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    MetallicShaderCard(
                        id = R.drawable.yunho_lee_business_card,
                        modifier = Modifier
                            .height(350.dp)
                            .aspectRatio(1 / 1.58f)
                            .clip(RoundedCornerShape(20.dp))
                    )
                }
            }

            composable<AnimatedGradient> {
                AnimatedGradient(modifier = Modifier.fillMaxSize())
            }

            composable<PullToRefreshWithNestedScroll> {
                PullToRefresh(modifier = Modifier.fillMaxSize())
            }

            composable<PinchZoom> {
                Image(
                    painter = painterResource(samples.first()),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3 / 5f)
                        .clip(RoundedCornerShape(20.dp))
                        .pinchZoom()
                )
            }
        }
    }
}
