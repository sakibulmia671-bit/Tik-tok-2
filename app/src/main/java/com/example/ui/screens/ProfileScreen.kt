package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.VideoItem
import com.example.ui.components.formatCount
import com.example.ui.theme.TikTokBlue
import com.example.ui.theme.TikTokCardDark
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokDarkSurface
import com.example.ui.theme.TikTokRed
import com.example.ui.theme.TikTokTextSecondary

@Composable
fun ProfileScreen(
    videos: List<VideoItem>,
    onVideoClick: (VideoItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Videos, 1: Liked, 2: Saved
    var isBengaliLanguage by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        // Profile Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = "Find Friends",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Sakibul Hasan",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            // Language Toggle Button (বাংলা / EN)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(TikTokCardDark)
                    .clickable { isBengaliLanguage = !isBengaliLanguage }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .testTag("lang_toggle_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Translate,
                    contentDescription = null,
                    tint = TikTokCyan,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isBengaliLanguage) "বাংলা" else "EN",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Profile Info Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar
            Box(
                modifier = Modifier.size(92.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_tiktok_creator2_1791409599435),
                    contentDescription = "Profile Photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .border(2.dp, TikTokRed, CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Handle & Verified
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "@sakibul_official",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Verified",
                    tint = TikTokBlue,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Stats Row: Following | Followers | Likes
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileStatItem(count = "142", label = if (isBengaliLanguage) "অনুসরণ" else "Following")
                Box(modifier = Modifier.height(20.dp).width(1.dp).background(Color.White.copy(alpha = 0.2f)))
                ProfileStatItem(count = "48.6K", label = if (isBengaliLanguage) "অনুসারী" else "Followers")
                Box(modifier = Modifier.height(20.dp).width(1.dp).background(Color.White.copy(alpha = 0.2f)))
                ProfileStatItem(count = "982.4K", label = if (isBengaliLanguage) "লাইক" else "Likes")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons: Edit Profile & Share Profile
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = TikTokCardDark),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("edit_profile_button")
                ) {
                    Text(
                        text = if (isBengaliLanguage) "প্রোফাইল সম্পাদনা" else "Edit Profile",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = TikTokCardDark),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("share_profile_button")
                ) {
                    Text(
                        text = if (isBengaliLanguage) "প্রোফাইল শেয়ার" else "Share Profile",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(TikTokCardDark)
                        .clickable {},
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = "Saved",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Bio
            Text(
                text = if (isBengaliLanguage)
                    "🎥 ডিজিটাল ক্রিয়েটর | ঢাকা, বাংলাদেশ 🇧🇩\n✨ প্রতিদিন নতুন ও মজার ভিডিও কনটেন্ট!\n📩 যোগাযোগ: contact@sakibul.bd"
                else
                    "🎥 Digital Creator | Dhaka, Bangladesh 🇧🇩\n✨ Daily entertaining & viral short videos!\n📩 Contact: contact@sakibul.bd",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Profile Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Black,
            contentColor = Color.White,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = Color.White,
                    height = 2.dp
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Videocam,
                        contentDescription = "Videos",
                        tint = if (selectedTab == 0) Color.White else TikTokTextSecondary
                    )
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Liked",
                        tint = if (selectedTab == 1) Color.White else TikTokTextSecondary
                    )
                }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Private",
                        tint = if (selectedTab == 2) Color.White else TikTokTextSecondary
                    )
                }
            )
        }

        // 3-Column Video Grid
        val displayedVideos = when (selectedTab) {
            1 -> videos.filter { it.isLiked }.ifEmpty { videos.take(2) }
            2 -> videos.filter { it.isBookmarked }.ifEmpty { videos.take(1) }
            else -> videos
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 2.dp, bottom = 70.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(displayedVideos) { video ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.75f)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(video.bgGradient.firstOrNull() ?: 0xFF1F1C2C),
                                    Color(video.bgGradient.lastOrNull() ?: 0xFF928DAB)
                                )
                            )
                        )
                        .clickable { onVideoClick(video) }
                ) {
                    // Play icon in center
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.Center)
                    )

                    // Views count at bottom left
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = formatCount(video.likesCount * 3),
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileStatItem(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            color = TikTokTextSecondary,
            fontSize = 11.sp
        )
    }
}
