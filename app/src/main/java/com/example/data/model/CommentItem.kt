package com.example.data.model

data class CommentItem(
    val id: String,
    val videoId: String,
    val authorName: String,
    val authorHandle: String,
    val authorAvatarRes: Int,
    val text: String,
    val timestamp: String,
    val likesCount: Int,
    val isLiked: Boolean = false,
    val isCreator: Boolean = false
)
