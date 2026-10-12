package com.example.data.model

data class VideoItem(
    val id: String,
    val videoUrl: String,
    val authorName: String,
    val authorHandle: String,
    val authorAvatarRes: Int,
    val caption: String,
    val captionBn: String,
    val tags: List<String>,
    val soundTitle: String,
    val soundArtist: String,
    val soundCoverRes: Int,
    val likesCount: Long,
    val commentsCount: Long,
    val bookmarksCount: Long,
    val sharesCount: Long,
    val isLiked: Boolean = false,
    val isBookmarked: Boolean = false,
    val isFollowing: Boolean = false,
    val bgGradient: List<Long> = listOf(0xFF1F1C2C, 0xFF928DAB)
)
