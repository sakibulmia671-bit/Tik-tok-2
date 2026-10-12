package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.VideoItem
import com.example.ui.theme.TikTokBlue

@Composable
fun BottomInfoOverlay(
    video: VideoItem,
    onSoundClick: () -> Unit,
    onTagClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    var showTranslation by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 90.dp, bottom = 18.dp)
    ) {
        // Creator handle and verified badge
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = video.authorHandle,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Verified",
                tint = TikTokBlue,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Caption & Tags
        val displayedCaption = if (showTranslation) video.captionBn else video.caption
        Text(
            text = displayedCaption,
            color = Color.White.copy(alpha = 0.95f),
            fontSize = 14.sp,
            maxLines = if (isExpanded) 6 else 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.clickable { isExpanded = !isExpanded }
        )

        // Hashtags Row
        Row(
            modifier = Modifier.padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            video.tags.take(3).forEach { tag ->
                Text(
                    text = "$tag ",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    modifier = Modifier.clickable { onTagClick(tag) }
                )
            }
        }

        // Translation Toggle
        Text(
            text = if (showTranslation) "See original / মূল দেখুন" else "See translation / অনুবাদ দেখুন",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .clickable { showTranslation = !showTranslation }
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Audio Marquee Ticker
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.3f))
                .clickable { onSoundClick() }
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.MusicNote,
                contentDescription = "Sound",
                tint = Color.White,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            MarqueeSoundText(
                soundText = "${video.soundTitle} - ${video.soundArtist}"
            )
        }
    }
}

@Composable
private fun MarqueeSoundText(
    soundText: String
) {
    val infiniteTransition = rememberInfiniteTransition(label = "marquee")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -120f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset"
    )

    Box(
        modifier = Modifier
            .width(160.dp)
            .height(18.dp)
    ) {
        Text(
            text = "$soundText   •   $soundText",
            color = Color.White,
            fontSize = 12.sp,
            maxLines = 1,
            modifier = Modifier.offset { IntOffset(offset.toInt(), 0) }
        )
    }
}
