package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.ui.theme.TikTokRed
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random

data class HeartBurst(
    val id: Long = System.currentTimeMillis() + Random.nextLong(1000),
    val position: Offset,
    val rotation: Float = Random.nextFloat() * 40f - 20f
)

@Composable
fun FloatingHeartOverlay(
    hearts: List<HeartBurst>,
    onHeartFinished: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        hearts.forEach { heart ->
            SingleAnimatedHeart(
                heart = heart,
                onFinished = { onHeartFinished(heart.id) }
            )
        }
    }
}

@Composable
private fun SingleAnimatedHeart(
    heart: HeartBurst,
    onFinished: () -> Unit
) {
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(1f) }
    val offsetY = remember { Animatable(0f) }

    LaunchedEffect(heart.id) {
        // Fast pop in
        scale.animateTo(
            targetValue = 1.3f,
            animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
        )
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 100)
        )
        // Float up and fade out
        launchHeartAnimation(offsetY, alpha)
        onFinished()
    }

    Icon(
        imageVector = Icons.Filled.Favorite,
        contentDescription = null,
        tint = TikTokRed,
        modifier = Modifier
            .offset {
                IntOffset(
                    x = (heart.position.x - 48).roundToInt(),
                    y = (heart.position.y - 48 + offsetY.value).roundToInt()
                )
            }
            .scale(scale.value)
            .alpha(alpha.value)
            .rotate(heart.rotation)
            .size(96.dp)
    )
}

private suspend fun launchHeartAnimation(
    offsetY: Animatable<Float, *>,
    alpha: Animatable<Float, *>
) = coroutineScope {
    launch {
        offsetY.animateTo(
            targetValue = -250f,
            animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing)
        )
    }
    launch {
        delay(350)
        alpha.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 350)
        )
    }
}
