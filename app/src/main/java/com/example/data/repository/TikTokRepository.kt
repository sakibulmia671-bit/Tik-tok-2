package com.example.data.repository

import com.example.R
import com.example.data.model.CommentItem
import com.example.data.model.NotificationItem
import com.example.data.model.NotificationType
import com.example.data.model.VideoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TikTokRepository {

    private val sampleVideos = listOf(
        VideoItem(
            id = "v_1",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            authorName = "Sakibul Hasan",
            authorHandle = "@sakibul_official",
            authorAvatarRes = R.drawable.img_tiktok_creator2_1791409599435,
            caption = "ঢাকা শহরের সূর্যাস্তের অসাধারণ রূপ! কার কার এই ভিউ ভালো লাগে? 🌆🇧🇩",
            captionBn = "ঢাকা শহরের সূর্যাস্তের অসাধারণ রূপ! কার কার এই ভিউ ভালো লাগে? 🌆🇧🇩",
            tags = listOf("#dhakavibes", "#bangladesh", "#sunset", "#viral", "#foryou"),
            soundTitle = "Bangla Acoustic Beat",
            soundArtist = "Tahsan Khan (Remix)",
            soundCoverRes = R.drawable.img_music_cover_1791409613220,
            likesCount = 384500,
            commentsCount = 4210,
            bookmarksCount = 28900,
            sharesCount = 12500,
            isLiked = false,
            isBookmarked = false,
            isFollowing = true,
            bgGradient = listOf(0xFF2C3E50, 0xFFFD746C)
        ),
        VideoItem(
            id = "v_2",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            authorName = "Samira Rahman",
            authorHandle = "@samira_dance",
            authorAvatarRes = R.drawable.img_tiktok_creator1_1791409586156,
            caption = "New dance trend unlocked! 💃 Did you try this step yet?",
            captionBn = "নতুন নাচের ট্রেন্ড! 💃 তোমরা কি এই স্টেপ ট্রাই করেছো?",
            tags = listOf("#dancechallenge", "#tiktoktrend", "#groove", "#bangla", "#fyp"),
            soundTitle = "Lofi Bangla Chill Beat",
            soundArtist = "Prod. BeatMaster BD",
            soundCoverRes = R.drawable.img_music_cover_1791409613220,
            likesCount = 892100,
            commentsCount = 9830,
            bookmarksCount = 54200,
            sharesCount = 31400,
            isLiked = true,
            isBookmarked = true,
            isFollowing = false,
            bgGradient = listOf(0xFF134E5E, 0xFF71B280)
        ),
        VideoItem(
            id = "v_3",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
            authorName = "Foodie Dhaka",
            authorHandle = "@foodie_dhaka",
            authorAvatarRes = R.drawable.img_tiktok_creator2_1791409599435,
            caption = "পুরান ঢাকার ঐতিহ্যবাহী কাচ্চি বিরিয়ানি টেস্ট রিভিউ! কার ক্ষুধা লাগলো? 🤤🍗",
            captionBn = "পুরান ঢাকার ঐতিহ্যবাহী কাচ্চি বিরিয়ানি টেস্ট রিভিউ! কার ক্ষুধা লাগলো? 🤤🍗",
            tags = listOf("#foodreview", "#kacchi", "#olddhaka", "#foodie", "#banglafood"),
            soundTitle = "Delicious Beats",
            soundArtist = "Original Food Sound",
            soundCoverRes = R.drawable.img_tiktok_icon_1791409526974,
            likesCount = 512000,
            commentsCount = 7620,
            bookmarksCount = 41300,
            sharesCount = 18900,
            isLiked = false,
            isBookmarked = false,
            isFollowing = false,
            bgGradient = listOf(0xFFE65C00, 0xFFF9D423)
        ),
        VideoItem(
            id = "v_4",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyBlazes.mp4",
            authorName = "Tech Tanvir",
            authorHandle = "@tech_tanvir",
            authorAvatarRes = R.drawable.img_tiktok_creator1_1791409586156,
            caption = "Top 3 hidden Android features you never knew existed! 📱🔥 Check them out now!",
            captionBn = "টপ ৩টি গোপন অ্যান্ড্রয়েড ফিচার যা আপনি কখনো জানতেন না! 📱🔥",
            tags = listOf("#techhacks", "#androidtips", "#techtok", "#tricks", "#viraltech"),
            soundTitle = "Tech Cyber Synthwave",
            soundArtist = "Tanvir Beats",
            soundCoverRes = R.drawable.img_music_cover_1791409613220,
            likesCount = 641800,
            commentsCount = 5340,
            bookmarksCount = 89100,
            sharesCount = 22400,
            isLiked = false,
            isBookmarked = false,
            isFollowing = true,
            bgGradient = listOf(0xFF0F2027, 0xFF203A43, 0xFF2C5364)
        ),
        VideoItem(
            id = "v_5",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4",
            authorName = "Comedy BD Club",
            authorHandle = "@comedy_bangla",
            authorAvatarRes = R.drawable.img_tiktok_creator2_1791409599435,
            caption = "পরীক্ষার আগের রাতে বন্ধুদের গ্রুপ স্টাডি যেমন হয়! 😂 Tag your friends!",
            captionBn = "পরীক্ষার আগের রাতে বন্ধুদের গ্রুপ স্টাডি যেমন হয়! 😂 বন্ধুদের ট্যাগ করুন!",
            tags = listOf("#comedy", "#groupstudy", "#banglacomedy", "#funnymoments", "#students"),
            soundTitle = "Funny Laugh Beat FX",
            soundArtist = "Original Laugh Track",
            soundCoverRes = R.drawable.img_tiktok_icon_1791409526974,
            likesCount = 1240000,
            commentsCount = 18400,
            bookmarksCount = 67200,
            sharesCount = 98100,
            isLiked = false,
            isBookmarked = false,
            isFollowing = false,
            bgGradient = listOf(0xFF8E2DE2, 0xFF4A00E0)
        )
    )

    private val _videos = MutableStateFlow<List<VideoItem>>(sampleVideos)
    val videos: StateFlow<List<VideoItem>> = _videos.asStateFlow()

    private val _comments = MutableStateFlow<Map<String, List<CommentItem>>>(
        mapOf(
            "v_1" to listOf(
                CommentItem(
                    id = "c_1",
                    videoId = "v_1",
                    authorName = "Abrar Fahim",
                    authorHandle = "@abrar_fahim",
                    authorAvatarRes = R.drawable.img_tiktok_creator1_1791409586156,
                    text = "হাতিরঝিলের সূর্যাস্ত দেখতে সবথেকে সেরা লাগে! 😍",
                    timestamp = "2h ago",
                    likesCount = 284,
                    isLiked = false
                ),
                CommentItem(
                    id = "c_2",
                    videoId = "v_1",
                    authorName = "Nusrat Jahan",
                    authorHandle = "@nusrat_j",
                    authorAvatarRes = R.drawable.img_tiktok_creator1_1791409586156,
                    text = "Background musicটা দারুণ মানিয়েছে ভাইয়া! 🎵👌",
                    timestamp = "4h ago",
                    likesCount = 142,
                    isLiked = true
                ),
                CommentItem(
                    id = "c_3",
                    videoId = "v_1",
                    authorName = "Sakibul Hasan",
                    authorHandle = "@sakibul_official",
                    authorAvatarRes = R.drawable.img_tiktok_creator2_1791409599435,
                    text = "ধন্যবাদ সবাইকে এত সুন্দর ভালোবাসার জন্য! ❤️🙌",
                    timestamp = "1h ago",
                    likesCount = 590,
                    isLiked = false,
                    isCreator = true
                )
            ),
            "v_2" to listOf(
                CommentItem(
                    id = "c_21",
                    videoId = "v_2",
                    authorName = "Tania Akter",
                    authorHandle = "@tania_dance",
                    authorAvatarRes = R.drawable.img_tiktok_creator2_1791409599435,
                    text = "Step গুলো এত স্মুথ কিভাবে করেন আপু?! 👏🔥",
                    timestamp = "5h ago",
                    likesCount = 412,
                    isLiked = false
                ),
                CommentItem(
                    id = "c_22",
                    videoId = "v_2",
                    authorName = "Mahir Hossain",
                    authorHandle = "@mahir_h",
                    authorAvatarRes = R.drawable.img_tiktok_creator1_1791409586156,
                    text = "Next video te tutorial please! 🤩",
                    timestamp = "1d ago",
                    likesCount = 188,
                    isLiked = false
                )
            )
        )
    )
    val comments: StateFlow<Map<String, List<CommentItem>>> = _comments.asStateFlow()

    private val _notifications = MutableStateFlow<List<NotificationItem>>(
        listOf(
            NotificationItem(
                id = "n_1",
                type = NotificationType.LIKE,
                senderName = "Samira Rahman",
                senderHandle = "@samira_dance",
                senderAvatarRes = R.drawable.img_tiktok_creator1_1791409586156,
                message = "liked your video: 'ঢাকা শহরের সূর্যাস্তের অসাধারণ রূপ!'",
                timeAgo = "10m ago"
            ),
            NotificationItem(
                id = "n_2",
                type = NotificationType.COMMENT,
                senderName = "Abrar Fahim",
                senderHandle = "@abrar_fahim",
                senderAvatarRes = R.drawable.img_tiktok_creator2_1791409599435,
                message = "commented: 'হাতিরঝিলের সূর্যাস্ত দেখতে সবথেকে সেরা লাগে! 😍'",
                timeAgo = "2h ago"
            ),
            NotificationItem(
                id = "n_3",
                type = NotificationType.FOLLOW,
                senderName = "Tech Tanvir",
                senderHandle = "@tech_tanvir",
                senderAvatarRes = R.drawable.img_tiktok_creator1_1791409586156,
                message = "started following you.",
                timeAgo = "5h ago"
            ),
            NotificationItem(
                id = "n_4",
                type = NotificationType.SYSTEM,
                senderName = "TikTok Team",
                senderHandle = "@tiktok",
                senderAvatarRes = R.drawable.img_tiktok_icon_1791409526974,
                message = "Welcome to TikTok! Explore trending sounds and creators around you.",
                timeAgo = "1d ago"
            )
        )
    )
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    fun toggleLike(videoId: String) {
        _videos.update { list ->
            list.map { video ->
                if (video.id == videoId) {
                    val newLiked = !video.isLiked
                    val newCount = if (newLiked) video.likesCount + 1 else (video.likesCount - 1).coerceAtLeast(0)
                    video.copy(isLiked = newLiked, likesCount = newCount)
                } else video
            }
        }
    }

    fun setLiked(videoId: String, liked: Boolean) {
        _videos.update { list ->
            list.map { video ->
                if (video.id == videoId) {
                    if (video.isLiked != liked) {
                        val newCount = if (liked) video.likesCount + 1 else (video.likesCount - 1).coerceAtLeast(0)
                        video.copy(isLiked = liked, likesCount = newCount)
                    } else video
                } else video
            }
        }
    }

    fun toggleBookmark(videoId: String) {
        _videos.update { list ->
            list.map { video ->
                if (video.id == videoId) {
                    val newSaved = !video.isBookmarked
                    val newCount = if (newSaved) video.bookmarksCount + 1 else (video.bookmarksCount - 1).coerceAtLeast(0)
                    video.copy(isBookmarked = newSaved, bookmarksCount = newCount)
                } else video
            }
        }
    }

    fun toggleFollow(authorHandle: String) {
        _videos.update { list ->
            list.map { video ->
                if (video.authorHandle == authorHandle) {
                    video.copy(isFollowing = !video.isFollowing)
                } else video
            }
        }
    }

    fun addComment(videoId: String, text: String, userHandle: String = "@my_profile") {
        if (text.isBlank()) return
        val newComment = CommentItem(
            id = "c_${System.currentTimeMillis()}",
            videoId = videoId,
            authorName = "You",
            authorHandle = userHandle,
            authorAvatarRes = R.drawable.img_tiktok_creator2_1791409599435,
            text = text.trim(),
            timestamp = "Just now",
            likesCount = 0,
            isLiked = false
        )

        _comments.update { map ->
            val existing = map[videoId] ?: emptyList()
            map + (videoId to listOf(newComment) + existing)
        }

        _videos.update { list ->
            list.map { video ->
                if (video.id == videoId) {
                    video.copy(commentsCount = video.commentsCount + 1)
                } else video
            }
        }
    }

    fun toggleCommentLike(videoId: String, commentId: String) {
        _comments.update { map ->
            val list = map[videoId] ?: return@update map
            val updated = list.map { comment ->
                if (comment.id == commentId) {
                    val newLiked = !comment.isLiked
                    val newCount = if (newLiked) comment.likesCount + 1 else (comment.likesCount - 1).coerceAtLeast(0)
                    comment.copy(isLiked = newLiked, likesCount = newCount)
                } else comment
            }
            map + (videoId to updated)
        }
    }

    fun addNewVideo(
        caption: String,
        tags: List<String>,
        soundTitle: String = "My Original Sound - Audio BD",
        videoUrl: String = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
    ) {
        val newVideo = VideoItem(
            id = "v_${System.currentTimeMillis()}",
            videoUrl = videoUrl,
            authorName = "You (Sakibul)",
            authorHandle = "@my_profile",
            authorAvatarRes = R.drawable.img_tiktok_creator2_1791409599435,
            caption = caption,
            captionBn = caption,
            tags = tags.ifEmpty { listOf("#foryou", "#bangla", "#viral") },
            soundTitle = soundTitle,
            soundArtist = "Original Creator Audio",
            soundCoverRes = R.drawable.img_music_cover_1791409613220,
            likesCount = 1,
            commentsCount = 0,
            bookmarksCount = 0,
            sharesCount = 0,
            isLiked = true,
            isBookmarked = false,
            isFollowing = true,
            bgGradient = listOf(0xFF200122, 0xFF6F0000)
        )

        _videos.update { listOf(newVideo) + it }
    }
}
