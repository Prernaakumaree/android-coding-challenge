package com.syftapp.codetest.data.api

import com.syftapp.codetest.data.model.api.*
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.reactivex.Single
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class BlogApiTest {

    @RelaxedMockK
    lateinit var blogService: BlogService

    @RelaxedMockK
    lateinit var sut : BlogApi

    private val latlong = LatLong("lat", "long")
    private val company = Company("name", "catch", "bs")
    private val address = Address("street", "suite", "city", "zip", latlong)

    private val anyUser = User(1, "name", "username", "email", address, "phone", "web", company)
    private val anyPost = Post(1, 1, "title", "body")
    private val anyComment = Comment(1, 1, "name" , "email", "body")

    private val anyDomainUser =
        com.syftapp.codetest.data.model.domain.User(1, "name", "username", "email")
    private val anyDomainPost = com.syftapp.codetest.data.model.domain.Post(1, 1, "title", "body")
    private val anyDomainComment = com.syftapp.codetest.data.model.domain.Comment(1, 1, "name", "email", "body")

    @Before
    fun setup() = MockKAnnotations.init(this)

    @Test
    fun `get users contains correct domain models`() {
        every { blogService.getUsers() } returns Single.just(listOf(anyUser))
        every { sut.getUsers() } returns Single.just(listOf(anyDomainUser))

        val apiUser = rxValue(blogService.getUsers()).get(0)
        val users = rxValue(sut.getUsers())

        assertThat(users)
            .hasSize(1)
            .contains(
                com.syftapp.codetest.data.model.domain.User(
                    id = apiUser.id,
                    name = apiUser.name,
                    username = apiUser.username,
                    email = apiUser.email

                )
            )
    }

    @Test
    fun `get posts contains correct domain models`() {
        every { blogService.getPosts() } returns Single.just(listOf(anyPost))
        every { sut.getPosts() } returns Single.just(listOf(anyDomainPost))

        val apiPost = rxValue(blogService.getPosts()).get(0)
        val posts = rxValue(sut.getPosts())

        assertThat(posts)
            .hasSize(1)
            .contains(
                com.syftapp.codetest.data.model.domain.Post(
                    id = apiPost.id,
                    userId = apiPost.userId,
                    title = apiPost.title,
                    body = apiPost.body
                )
            )
    }

    @Test
    fun `get comments contains correct domain models`() {
        every { blogService.getComments() } returns Single.just(listOf(anyComment))
        every { sut.getComments() } returns Single.just(listOf(anyDomainComment))

        val apiComment = rxValue(blogService.getComments()).get(0)
        val comments = rxValue(sut.getComments())

        assertThat(comments)
            .hasSize(1)
            .contains(
                com.syftapp.codetest.data.model.domain.Comment(
                    id = apiComment.id,
                    postId = apiComment.postId,
                    name = apiComment.name,
                    email = apiComment.email,
                    body = apiComment.body
                )
            )
    }

    private fun <T> rxValue(apiItem: Single<T>): T = apiItem.test().values().get(0)
}