package com.example.ui.components

import android.media.MediaPlayer
import android.net.Uri
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.model.VideoItem
import kotlin.math.sin

@Composable
fun VideoPlayerView(
    video: VideoItem,
    isActive: Boolean,
    onSingleTap: () -> Unit,
    onDoubleTap: (Offset) -> Unit,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isVideoPrepared by remember { mutableStateOf(false) }
    var hasError by remember { mutableStateOf(false) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    // Double tap vs single tap gesture detector
    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(video.id) {
                detectTapGestures(
                    onDoubleTap = { offset ->
                        onDoubleTap(offset)
                    },
                    onTap = {
                        onSingleTap()
                    }
                )
            }
    ) {
        // Ambient / Fallback Dynamic Animated Canvas (always underneath)
        VideoAmbientBackground(
            colors = video.bgGradient,
            isPlaying = isPlaying && isActive
        )

        // Native MediaPlayer + SurfaceView
        if (isActive) {
            AndroidView(
                factory = { ctx ->
                    SurfaceView(ctx).apply {
                        holder.addCallback(object : SurfaceHolder.Callback {
                            override fun surfaceCreated(holder: SurfaceHolder) {
                                try {
                                    val player = MediaPlayer().apply {
                                        setDisplay(holder)
                                        setDataSource(ctx, Uri.parse(video.videoUrl))
                                        isLooping = true
                                        setOnPreparedListener { mp ->
                                            isVideoPrepared = true
                                            if (isPlaying) {
                                                mp.start()
                                            }
                                        }
                                        setOnErrorListener { _, _, _ ->
                                            hasError = true
                                            true
                                        }
                                        prepareAsync()
                                    }
                                    mediaPlayer = player
                                } catch (e: Exception) {
                                    hasError = true
                                }
                            }

                            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

                            override fun surfaceDestroyed(holder: SurfaceHolder) {
                                mediaPlayer?.release()
                                mediaPlayer = null
                                isVideoPrepared = false
                            }
                        })
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            // Play / Pause controller syncing
            LaunchedEffect(isPlaying, isVideoPrepared) {
                mediaPlayer?.let { player ->
                    if (isVideoPrepared) {
                        if (isPlaying && !player.isPlaying) {
                            player.start()
                        } else if (!isPlaying && player.isPlaying) {
                            player.pause()
                        }
                    }
                }
            }
        }

        // Clean up media player when inactive
        DisposableEffect(isActive) {
            onDispose {
                mediaPlayer?.release()
                mediaPlayer = null
                isVideoPrepared = false
            }
        }

        // Paused Indicator Badge Overlay
        AnimatedVisibility(
            visible = !isPlaying,
            enter = fadeIn(tween(150)) + scaleIn(tween(150)),
            exit = fadeOut(tween(150)) + scaleOut(tween(150)),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .size(74.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.45f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.size(46.dp)
                )
            }
        }
    }
}

@Composable
private fun VideoAmbientBackground(
    colors: List<Long>,
    isPlaying: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    val color1 = Color(colors.getOrElse(0) { 0xFF0F2027 })
    val color2 = Color(colors.getOrElse(1) { 0xFF203A43 })
    val color3 = Color(colors.getOrElse(2) { 0xFF2C5364 })

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        val offsetY = if (isPlaying) sin(phase) * 60f else 0f
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    color1,
                    color2,
                    color3,
                    Color.Black
                ),
                startY = 0f,
                endY = height + offsetY
            )
        )

        // Subtle glowing atmospheric circles
        val waveFactor = sin(phase) * 30f
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(color2.copy(alpha = 0.35f), Color.Transparent),
                center = Offset(width * 0.5f, height * 0.4f + waveFactor),
                radius = width * 0.7f
            ),
            center = Offset(width * 0.5f, height * 0.4f + waveFactor),
            radius = width * 0.7f
        )
    }
}
