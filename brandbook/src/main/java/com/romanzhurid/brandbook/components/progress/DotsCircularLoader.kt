package com.romanzhurid.brandbook.components.progress

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.romanzhurid.brandbook.theme.AppTheme
import kotlin.math.cos
import kotlin.math.sin

@Preview(showBackground = true)
@Composable
fun DotsCircularLoaderPreview() {
    AppTheme {
        DotsCircularLoader()
    }
}

@Composable
fun DotsCircularLoader(
    modifier: Modifier = Modifier,
    color: Color = AppTheme.colorScheme.primary,
    durationMillis: Int = 800
) {
    val dotsCount = 8
    val dotSize: Dp = AppTheme.dimensions.small
    val radius: Dp = AppTheme.dimensions.xMedium
    val infiniteTransition = rememberInfiniteTransition(label = "dots_loader")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing)
        ),
        label = "rotation"
    )

    Canvas(
        modifier = modifier
            .size((radius * 2) + dotSize)
    ) {
        val angleStep = 360f / dotsCount
        val center = this.center
        val r = radius.toPx()
        val dotRadius = dotSize.toPx() / 2

        repeat(dotsCount) { index ->
            val angle = Math.toRadians(
                (angleStep * index + rotation).toDouble()
            )

            val x = center.x + r * cos(angle).toFloat()
            val y = center.y + r * sin(angle).toFloat()

            val alpha = (index + 1) / dotsCount.toFloat()

            drawCircle(
                color = color.copy(alpha = alpha),
                radius = dotRadius,
                center = Offset(x, y)
            )
        }
    }
}
