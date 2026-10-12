package com.example

import com.example.data.repository.TikTokRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testRepositoryInitialVideos() {
        val repo = TikTokRepository()
        val videos = repo.videos.value
        assertTrue("Repository should have sample videos", videos.isNotEmpty())
        assertEquals("v_1", videos[0].id)
    }

    @Test
    fun testToggleLike() {
        val repo = TikTokRepository()
        val initialVideo = repo.videos.value.first { it.id == "v_1" }
        val initialLikes = initialVideo.likesCount
        assertFalse(initialVideo.isLiked)

        repo.toggleLike("v_1")
        val likedVideo = repo.videos.value.first { it.id == "v_1" }
        assertTrue(likedVideo.isLiked)
        assertEquals(initialLikes + 1, likedVideo.likesCount)

        repo.toggleLike("v_1")
        val unlikedVideo = repo.videos.value.first { it.id == "v_1" }
        assertFalse(unlikedVideo.isLiked)
        assertEquals(initialLikes, unlikedVideo.likesCount)
    }

    @Test
    fun testAddComment() {
        val repo = TikTokRepository()
        val initialCount = repo.videos.value.first { it.id == "v_1" }.commentsCount

        repo.addComment("v_1", "অসাধারণ ভিডিও ভাইয়া! 🔥")
        val updatedComments = repo.comments.value["v_1"]
        assertTrue(updatedComments != null && updatedComments.isNotEmpty())
        assertEquals("অসাধারণ ভিডিও ভাইয়া! 🔥", updatedComments?.first()?.text)

        val updatedVideo = repo.videos.value.first { it.id == "v_1" }
        assertEquals(initialCount + 1, updatedVideo.commentsCount)
    }

    @Test
    fun testAddNewVideo() {
        val repo = TikTokRepository()
        val initialSize = repo.videos.value.size

        repo.addNewVideo(
            caption = "আমার প্রথম টিকটক ভিডিও 🇧🇩",
            tags = listOf("#foryou", "#bangla")
        )

        val updatedVideos = repo.videos.value
        assertEquals(initialSize + 1, updatedVideos.size)
        assertEquals("আমার প্রথম টিকটক ভিডিও 🇧🇩", updatedVideos.first().caption)
    }
}
