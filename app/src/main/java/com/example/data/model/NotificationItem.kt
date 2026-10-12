package com.example.data.model

enum class NotificationType {
    LIKE, COMMENT, FOLLOW, MENTION, SYSTEM
}

data class NotificationItem(
    val id: String,
    val type: NotificationType,
    val senderName: String,
    val senderHandle: String,
    val senderAvatarRes: Int,
    val message: String,
    val timeAgo: String,
    val videoPreviewRes: Int? = null,
    val isRead: Boolean = false
)
