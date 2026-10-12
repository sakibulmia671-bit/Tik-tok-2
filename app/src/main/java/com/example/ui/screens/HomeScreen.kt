package com.example.ui.screens

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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CommentItem
import com.example.data.model.VideoItem
import com.example.ui.components.BottomInfoOverlay
import com.example.ui.components.CommentBottomSheet
import com.example.ui.components.FloatingHeartOverlay
import com.example.ui.components.HeartBurst
import com.example.ui.components.RightActionBar
import com.example.ui.components.ShareBottomSheet
import com.example.ui.components.SoundDetailSheet
import com.example.ui.components.VideoPlayerView
import com.example.ui.theme.TikTokLiveRed
import com.example.ui.theme.TikTokRed
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    videos: List<VideoItem>,
    commentsMap: Map<String, List<CommentItem>>,
    onToggleLike: (String) -> Unit,
    onSetLiked: (String, Boolean) -> Unit,
    onToggleBookmark: (String) -> Unit,
    onToggleFollow: (String) -> Unit,
    onAddComment: (String, String) -> Unit,
    onToggleCommentLike: (String, String) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToCreateWithSound: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (videos.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize().background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text("No videos yet / কোনো ভিডিও নেই", color = Color.White)
        }
        return
    }

    val pagerState = rememberPagerState(pageCount = { videos.size })
    var selectedFeedTab by remember { mutableIntStateOf(1) } // 0 = Following, 1 = For You
    var isPlaying by remember { mutableStateOf(true) }
    val floatingHearts = remember { mutableStateListOf<HeartBurst>() }

    // Sheets state
    var activeCommentsVideoId by remember { mutableStateOf<String?>(null) }
    var activeShareVideo by remember { mutableStateOf<VideoItem?>(null) }
    var activeSoundVideo by remember { mutableStateOf<VideoItem?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Reset playing state on swipe
    LaunchedEffect(pagerState.currentPage) {
        isPlaying = true
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val video = videos[page]
            val isActivePage = pagerState.currentPage == page

            Box(modifier = Modifier.fillMaxSize()) {
                // Video Player & gestures
                VideoPlayerView(
                    video = video,
                    isActive = isActivePage,
                    isPlaying = isPlaying && isActivePage,
                    onSingleTap = {
                        isPlaying = !isPlaying
                    },
                    onDoubleTap = { tapOffset ->
                        floatingHearts.add(HeartBurst(position = tapOffset))
                        onSetLiked(video.id, true)
                    }
                )

                // Double Tap Hearts Burst Layer
                FloatingHeartOverlay(
                    hearts = floatingHearts,
                    onHeartFinished = { heartId ->
                        floatingHearts.removeAll { it.id == heartId }
                    }
                )

                // Right Action Bar
                RightActionBar(
                    video = video,
                    onLikeClick = { onToggleLike(video.id) },
                    onCommentClick = { activeCommentsVideoId = video.id },
                    onBookmarkClick = {
                        onToggleBookmark(video.id)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                if (!video.isBookmarked) "Added to Favorites / ফেভারিটে সংরক্ষিত!" else "Removed / সরানো হয়েছে"
                            )
                        }
                    },
                    onShareClick = { activeShareVideo = video },
                    onFollowClick = { onToggleFollow(video.authorHandle) },
                    onSoundClick = { activeSoundVideo = video },
                    isPlaying = isPlaying && isActivePage,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )

                // Bottom Info Overlay
                BottomInfoOverlay(
                    video = video,
                    onSoundClick = { activeSoundVideo = video },
                    onTagClick = { onNavigateToSearch() },
                    modifier = Modifier.align(Alignment.BottomStart)
                )

                // Video Progress Bar at Bottom
                LinearProgressIndicator(
                    progress = { 0.45f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .align(Alignment.BottomCenter),
                    color = Color.White.copy(alpha = 0.8f),
                    trackColor = Color.White.copy(alpha = 0.2f)
                )
            }
        }

        // Top Navigation Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // LIVE Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.35f))
                    .clickable {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("LIVE streaming starting soon! / লাইভ স্ট্রিম শীঘ্রই শুরু হচ্ছে!")
                        }
                    }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .testTag("live_button")
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(TikTokLiveRed)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "LIVE",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Tabs: Following | For You
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { selectedFeedTab = 0 }
                        .padding(horizontal = 12.dp)
                        .testTag("tab_following")
                ) {
                    Text(
                        text = "Following",
                        color = if (selectedFeedTab == 0) Color.White else Color.White.copy(alpha = 0.6f),
                        fontSize = 16.sp,
                        fontWeight = if (selectedFeedTab == 0) FontWeight.Bold else FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .height(2.dp)
                            .width(28.dp)
                            .background(if (selectedFeedTab == 0) Color.White else Color.Transparent)
                    )
                }

                Text(
                    text = "|",
                    color = Color.White.copy(alpha = 0.3f),
                    fontSize = 14.sp
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { selectedFeedTab = 1 }
                        .padding(horizontal = 12.dp)
                        .testTag("tab_foryou")
                ) {
                    Text(
                        text = "For You",
                        color = if (selectedFeedTab == 1) Color.White else Color.White.copy(alpha = 0.6f),
                        fontSize = 17.sp,
                        fontWeight = if (selectedFeedTab == 1) FontWeight.Bold else FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .height(2.dp)
                            .width(28.dp)
                            .background(if (selectedFeedTab == 1) Color.White else Color.Transparent)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Search Icon
            IconButton(
                onClick = onNavigateToSearch,
                modifier = Modifier.testTag("search_icon_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        // Snackbar Host for action confirmations
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.Center)
        )

        // Comment Bottom Sheet
        activeCommentsVideoId?.let { videoId ->
            val comments = commentsMap[videoId] ?: emptyList()
            CommentBottomSheet(
                comments = comments,
                onDismiss = { activeCommentsVideoId = null },
                onAddComment = { text ->
                    onAddComment(videoId, text)
                },
                onToggleCommentLike = { commentId ->
                    onToggleCommentLike(videoId, commentId)
                }
            )
        }

        // Share Bottom Sheet
        activeShareVideo?.let { _ ->
            ShareBottomSheet(
                onDismiss = { activeShareVideo = null },
                onActionSelected = { action ->
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Shared via $action! / শেয়ার সফল হয়েছে!")
                    }
                }
            )
        }

        // Sound Detail Bottom Sheet
        activeSoundVideo?.let { soundVideo ->
            SoundDetailSheet(
                video = soundVideo,
                onDismiss = { activeSoundVideo = null },
                onUseSound = { soundTitle ->
                    onNavigateToCreateWithSound(soundTitle)
                }
            )
        }
    }
}
