package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.TikTokRepository
import com.example.ui.screens.CreateVideoScreen
import com.example.ui.screens.DiscoverScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.InboxScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokDarkSurface
import com.example.ui.theme.TikTokRed
import com.example.ui.theme.TikTokTextSecondary

enum class TikTokTab(val titleEn: String, val titleBn: String) {
    HOME("Home", "হোম"),
    DISCOVER("Discover", "আবিষ্কার"),
    CREATE("Create", "তৈরি"),
    INBOX("Inbox", "ইনবক্স"),
    PROFILE("Profile", "প্রোফাইল")
}

@Composable
fun MainScreen(
    repository: TikTokRepository = remember { TikTokRepository() }
) {
    var currentTab by remember { mutableStateOf(TikTokTab.HOME) }
    var createSoundSelected by remember { mutableStateOf<String?>(null) }
    val videos by repository.videos.collectAsState()
    val commentsMap by repository.comments.collectAsState()
    val notifications by repository.notifications.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black,
        bottomBar = {
            if (currentTab != TikTokTab.CREATE) {
                TikTokBottomNav(
                    currentTab = currentTab,
                    onTabSelected = { tab ->
                        if (tab == TikTokTab.CREATE) {
                            createSoundSelected = null
                        }
                        currentTab = tab
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (currentTab != TikTokTab.CREATE) 56.dp else 0.dp)
        ) {
            when (currentTab) {
                TikTokTab.HOME -> {
                    HomeScreen(
                        videos = videos,
                        commentsMap = commentsMap,
                        onToggleLike = { repository.toggleLike(it) },
                        onSetLiked = { id, liked -> repository.setLiked(id, liked) },
                        onToggleBookmark = { repository.toggleBookmark(it) },
                        onToggleFollow = { repository.toggleFollow(it) },
                        onAddComment = { id, text -> repository.addComment(id, text) },
                        onToggleCommentLike = { vId, cId -> repository.toggleCommentLike(vId, cId) },
                        onNavigateToSearch = { currentTab = TikTokTab.DISCOVER },
                        onNavigateToCreateWithSound = { sound ->
                            createSoundSelected = sound
                            currentTab = TikTokTab.CREATE
                        }
                    )
                }

                TikTokTab.DISCOVER -> {
                    DiscoverScreen(
                        videos = videos,
                        onVideoSelected = { video ->
                            // Switch to Home to view video
                            currentTab = TikTokTab.HOME
                        },
                        onToggleFollow = { repository.toggleFollow(it) }
                    )
                }

                TikTokTab.CREATE -> {
                    CreateVideoScreen(
                        initialSound = createSoundSelected,
                        onClose = { currentTab = TikTokTab.HOME },
                        onPostVideo = { caption, tags, soundTitle ->
                            repository.addNewVideo(
                                caption = caption,
                                tags = tags,
                                soundTitle = soundTitle
                            )
                            currentTab = TikTokTab.HOME
                        }
                    )
                }

                TikTokTab.INBOX -> {
                    InboxScreen(notifications = notifications)
                }

                TikTokTab.PROFILE -> {
                    ProfileScreen(
                        videos = videos,
                        onVideoClick = {
                            currentTab = TikTokTab.HOME
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TikTokBottomNav(
    currentTab: TikTokTab,
    onTabSelected: (TikTokTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.95f))
            .windowInsetsPadding(WindowInsets.navigationBars)
            .height(56.dp)
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Home
        NavIconItem(
            label = "হোম",
            icon = if (currentTab == TikTokTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
            isSelected = currentTab == TikTokTab.HOME,
            testTag = "nav_home",
            onClick = { onTabSelected(TikTokTab.HOME) }
        )

        // Discover
        NavIconItem(
            label = "আবিষ্কার",
            icon = if (currentTab == TikTokTab.DISCOVER) Icons.Filled.Explore else Icons.Outlined.Explore,
            isSelected = currentTab == TikTokTab.DISCOVER,
            testTag = "nav_discover",
            onClick = { onTabSelected(TikTokTab.DISCOVER) }
        )

        // Iconic TikTok [+] Button
        TikTokCreateButton(
            onClick = { onTabSelected(TikTokTab.CREATE) }
        )

        // Inbox
        NavIconItem(
            label = "ইনবক্স",
            icon = if (currentTab == TikTokTab.INBOX) Icons.Filled.ChatBubble else Icons.Outlined.ChatBubbleOutline,
            isSelected = currentTab == TikTokTab.INBOX,
            hasBadge = true,
            badgeCount = 3,
            testTag = "nav_inbox",
            onClick = { onTabSelected(TikTokTab.INBOX) }
        )

        // Profile
        NavIconItem(
            label = "প্রোফাইল",
            icon = if (currentTab == TikTokTab.PROFILE) Icons.Filled.Person else Icons.Outlined.Person,
            isSelected = currentTab == TikTokTab.PROFILE,
            testTag = "nav_profile",
            onClick = { onTabSelected(TikTokTab.PROFILE) }
        )
    }
}

@Composable
private fun NavIconItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    hasBadge: Boolean = false,
    badgeCount: Int = 0,
    testTag: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable { onClick() }
            .testTag(testTag)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Box {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) Color.White else TikTokTextSecondary,
                modifier = Modifier.size(24.dp)
            )
            if (hasBadge) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(TikTokRed)
                        .align(Alignment.TopEnd)
                        .offset(x = 6.dp, y = (-2).dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = badgeCount.toString(),
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = label,
            color = if (isSelected) Color.White else TikTokTextSecondary,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun TikTokCreateButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(width = 46.dp, height = 30.dp)
            .clickable { onClick() }
            .testTag("nav_create_button"),
        contentAlignment = Alignment.Center
    ) {
        // Left Cyan Underlay
        Box(
            modifier = Modifier
                .size(width = 40.dp, height = 28.dp)
                .offset(x = (-3).dp)
                .clip(RoundedCornerShape(8.dp))
                .background(TikTokCyan)
        )

        // Right Red Underlay
        Box(
            modifier = Modifier
                .size(width = 40.dp, height = 28.dp)
                .offset(x = 3.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(TikTokRed)
        )

        // Center White Rounded Box
        Box(
            modifier = Modifier
                .size(width = 40.dp, height = 28.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create Video",
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
