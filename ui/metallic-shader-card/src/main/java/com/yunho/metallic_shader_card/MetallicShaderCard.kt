package com.yunho.metallic_shader_card

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MetallicShaderCard(
    @DrawableRes id: Int,
    modifier: Modifier = Modifier
) {
    val runtimeShader = remember {
        RuntimeShader(
            """
        uniform shader inputShader;
        uniform float2 u_resolution;
        uniform float u_rotationX;
        uniform float u_rotationY;

        half4 main(float2 fragCoord) {
            float2 uv = fragCoord / u_resolution;
            half4 baseColor = inputShader.eval(fragCoord);

            float radX = radians(u_rotationX);
            float radY = radians(u_rotationY);

            half3 lightDir = normalize(half3(
                sin(radY),
                sin(radX),
                0.8
            ));

            half3 N = normalize(half3(uv - 0.5, 1.0));
            half3 viewDir = normalize(half3(0.0, 0.0, 1.0));

            half diff = clamp(dot(N, lightDir), 0.0, 1.0);
            half3 reflectDir = reflect(-lightDir, N);
            half spec = pow(clamp(dot(reflectDir, viewDir), 0.0, 1.0), 32.0);

            half3 metalBase = half3(0.7, 0.75, 0.9);
            half3 finalColor = baseColor.rgb * (0.3 + 0.7 * diff) + spec * 0.1 * metalBase;

            return half4(finalColor, baseColor.a);
        }
        """.trimIndent()
        )
    }
    val (rotationX, rotationY) = rememberDeviceRotation()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = modifier
                .graphicsLayer {
                    clip = true
                    cameraDistance = 8 * density

                    runtimeShader.setFloatUniform("u_resolution", floatArrayOf(size.width, size.height))
                    runtimeShader.setFloatUniform("u_rotationX", rotationX)
                    runtimeShader.setFloatUniform("u_rotationY", rotationY)

                    renderEffect = RenderEffect
                        .createRuntimeShaderEffect(runtimeShader, "inputShader")
                        .asComposeRenderEffect()
                },
            painter = painterResource(id),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}
