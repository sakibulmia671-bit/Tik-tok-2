package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.VideoItem
import com.example.ui.theme.TikTokRed
import com.example.ui.theme.TikTokYellow
import kotlinx.coroutines.launch

@Composable
fun RightActionBar(
    video: VideoItem,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onShareClick: () -> Unit,
    onFollowClick: () -> Unit,
    onSoundClick: () -> Unit,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val likeScale = remember { Animatable(1f) }
    val bookmarkScale = remember { Animatable(1f) }

    Column(
        modifier = modifier.padding(end = 12.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Creator Avatar + Follow Button
        Box(
            modifier = Modifier.size(54.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                painter = painterResource(id = video.authorAvatarRes),
                contentDescription = "${video.authorName} avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, Color.White, CircleShape)
                    .testTag("avatar_button")
            )

            // Follow Button (+)
            androidx.compose.animation.AnimatedVisibility(
                visible = !video.isFollowing,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(TikTokRed)
                        .clickable { onFollowClick() }
                        .testTag("follow_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Follow",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Like Button
        ActionBarItem(
            icon = {
                Icon(
                    imageVector = if (video.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Like",
                    tint = if (video.isLiked) TikTokRed else Color.White,
                    modifier = Modifier
                        .size(36.dp)
                        .scale(likeScale.value)
                )
            },
            label = formatCount(video.likesCount),
            testTag = "like_button",
            onClick = {
                coroutineScope.launch {
                    likeScale.animateTo(1.35f, tween(100, easing = FastOutSlowInEasing))
                    likeScale.animateTo(1f, tween(100))
                }
                onLikeClick()
            }
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Comment Button
        ActionBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Filled.Message,
                    contentDescription = "Comments",
                    tint = Color.White,
                    modifier = Modifier.size(34.dp)
                )
            },
            label = formatCount(video.commentsCount),
            testTag = "comment_button",
            onClick = onCommentClick
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Bookmark Button
        ActionBarItem(
            icon = {
                Icon(
                    imageVector = if (video.isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Favorite",
                    tint = if (video.isBookmarked) TikTokYellow else Color.White,
                    modifier = Modifier
                        .size(34.dp)
                        .scale(bookmarkScale.value)
                )
            },
            label = formatCount(video.bookmarksCount),
            testTag = "bookmark_button",
            onClick = {
                coroutineScope.launch {
                    bookmarkScale.animateTo(1.3f, tween(100))
                    bookmarkScale.animateTo(1f, tween(100))
                }
                onBookmarkClick()
            }
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Share Button
        ActionBarItem(
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Reply,
                    contentDescription = "Share",
                    tint = Color.White,
                    modifier = Modifier.size(34.dp)
                )
            },
            label = formatCount(video.sharesCount),
            testTag = "share_button",
            onClick = onShareClick
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Rotating Music Disc
        RotatingSoundDisc(
            coverRes = video.soundCoverRes,
            isPlaying = isPlaying,
            onClick = onSoundClick
        )
    }
}

@Composable
private fun ActionBarItem(
    icon: @Composable () -> Unit,
    label: String,
    testTag: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        icon()
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun RotatingSoundDisc(
    coverRes: Int,
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "disc_rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = Modifier
            .size(48.dp)
            .clickable { onClick() }
            .testTag("sound_disc_button"),
        contentAlignment = Alignment.Center
    ) {
        // Outer Vinyl Disc
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFF1B1B1B))
                .border(6.dp, Color(0xFF282828), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Album art inside
            Image(
                painter = painterResource(id = coverRes),
                contentDescription = "Sound Disc",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .rotate(if (isPlaying) rotation else 0f)
            )
        }
    }
}

fun formatCount(count: Long): String {
    return when {
        count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
        count >= 1_000 -> String.format("%.1fK", count / 1_000.0)
        else -> count.toString()
    }
}
