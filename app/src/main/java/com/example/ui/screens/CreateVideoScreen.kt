package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TikTokCardDark
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokDarkSurface
import com.example.ui.theme.TikTokRed
import com.example.ui.theme.TikTokTextSecondary
import kotlinx.coroutines.delay

@Composable
fun CreateVideoScreen(
    initialSound: String? = null,
    onClose: () -> Unit,
    onPostVideo: (caption: String, tags: List<String>, soundTitle: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isRecording by remember { mutableStateOf(false) }
    var recordingProgress by remember { mutableFloatStateOf(0f) }
    var showPostDialog by remember { mutableStateOf(false) }
    var selectedSound by remember { mutableStateOf(initialSound ?: "Bangla Viral Hit - Pop Beat") }
    var selectedSpeed by remember { mutableStateOf("1x") }
    var isFrontCamera by remember { mutableStateOf(false) }
    var isFlashOn by remember { mutableStateOf(false) }
    var isBeautyOn by remember { mutableStateOf(true) }
    var selectedModeIndex by remember { mutableIntStateOf(0) } // 0: 15s, 1: 60s, 2: 10m

    // Post modal state
    var postCaption by remember { mutableStateOf("নতুন একটি টিকটক ভিডিও বানালাম! সবার মতামত আশা করছি ❤️🇧🇩") }
    val quickHashtags = listOf("#fyp", "#bangla", "#viral", "#foryou", "#trending", "#dhaka")

    // Handle recording animation timer
    LaunchedEffect(isRecording) {
        if (isRecording) {
            recordingProgress = 0f
            while (recordingProgress < 1f && isRecording) {
                delay(100)
                recordingProgress += 0.02f
            }
            if (recordingProgress >= 1f) {
                isRecording = false
                showPostDialog = true
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Simulated Camera Viewfinder with dynamic studio lighting gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            if (isFrontCamera) Color(0xFF1B2A47) else Color(0xFF2E1A47),
                            Color(0xFF0F0F14),
                            Color.Black
                        )
                    )
                )
        ) {
            // Camera grid line hints or lens styling
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
            )
        }

        // Top Camera Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onClose,
                modifier = Modifier.testTag("close_camera_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Add Sound Pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
                    .clickable { /* Select sound */ }
                    .testTag("add_sound_pill")
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = selectedSound,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = { isFlashOn = !isFlashOn },
                modifier = Modifier.testTag("flash_toggle")
            ) {
                Icon(
                    imageVector = Icons.Default.FlashOn,
                    contentDescription = "Flash",
                    tint = if (isFlashOn) TikTokCyan else Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        // Right Camera Controls
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CameraToolButton(
                icon = Icons.Default.Cameraswitch,
                label = "Flip",
                onClick = { isFrontCamera = !isFrontCamera }
            )
            CameraToolButton(
                icon = Icons.Default.Speed,
                label = selectedSpeed,
                onClick = {
                    selectedSpeed = when (selectedSpeed) {
                        "0.5x" -> "1x"
                        "1x" -> "2x"
                        "2x" -> "3x"
                        else -> "0.5x"
                    }
                }
            )
            CameraToolButton(
                icon = Icons.Default.AutoFixHigh,
                label = if (isBeautyOn) "Beauty" else "Off",
                tint = if (isBeautyOn) TikTokRed else Color.White,
                onClick = { isBeautyOn = !isBeautyOn }
            )
            CameraToolButton(
                icon = Icons.Default.Timer,
                label = "Timer",
                onClick = {}
            )
        }

        // Bottom Recording Controls
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mode Selectors (15s | 60s | 10m)
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                listOf("15s", "60s", "10m").forEachIndexed { index, mode ->
                    Text(
                        text = mode,
                        color = if (selectedModeIndex == index) Color.White else Color.White.copy(alpha = 0.5f),
                        fontSize = 14.sp,
                        fontWeight = if (selectedModeIndex == index) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.clickable { selectedModeIndex = index }
                    )
                }
            }

            // Record Button Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Effects / Filters icon
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { /* open filters */ }
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoFixHigh,
                            contentDescription = "Effects",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Effects", color = Color.White, fontSize = 11.sp)
                }

                // Big TikTok Record Button with animated outer ring
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clickable {
                            if (isRecording) {
                                isRecording = false
                                showPostDialog = true
                            } else {
                                isRecording = true
                            }
                        }
                        .testTag("record_button"),
                    contentAlignment = Alignment.Center
                ) {
                    // Outer Circle Border
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .border(4.dp, Color.White, CircleShape)
                    )

                    if (isRecording) {
                        CircularProgressIndicator(
                            progress = { recordingProgress },
                            color = TikTokRed,
                            strokeWidth = 4.dp,
                            modifier = Modifier.size(80.dp)
                        )
                        // Square stop indicator
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(TikTokRed)
                        )
                    } else {
                        // Inner red circle
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(TikTokRed)
                        )
                    }
                }

                // Upload Button (from gallery)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { showPostDialog = true }
                        .testTag("upload_button")
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "Upload",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Upload", color = Color.White, fontSize = 11.sp)
                }
            }
        }

        // Post Dialog Modal
        if (showPostDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(TikTokDarkSurface)
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Post Video / প্রকাশ করুন",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { showPostDialog = false }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = postCaption,
                        onValueChange = { postCaption = it },
                        label = { Text("Describe your video / ক্যাপশন দিন") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .testTag("post_caption_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = TikTokCardDark,
                            unfocusedContainerColor = TikTokCardDark,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedIndicatorColor = TikTokRed,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Quick hashtag chips
                    Text(
                        text = "Popular Hashtags:",
                        color = TikTokTextSecondary,
                        fontSize = 12.sp
                    )

                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        quickHashtags.take(4).forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(TikTokCardDark)
                                    .clickable { postCaption += " $tag" }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(tag, color = TikTokCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            showPostDialog = false
                            onPostVideo(
                                postCaption,
                                listOf("#foryou", "#bangla", "#viral"),
                                selectedSound
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TikTokRed),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("publish_video_button")
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Post to TikTok / পোস্ট করুন",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CameraToolButton(
    icon: ImageVector,
    label: String,
    tint: Color = Color.White,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.45f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
