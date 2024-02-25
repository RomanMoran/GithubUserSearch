package com.example.domain.models

data class GitHubUser(
    val login: String,
    val id: Int,
    val nodeId: String,
    val avatarUrl: String,
    val gravatarId: String,
    val url: String,
    val htmlUrl: String,
    val followersUrl: String,
    val followingUrl: String,
    val gistsUrl: String,
    val starredUrl: String,
    val subscriptionsUrl: String,
    val organizationsUrl: String,
    val reposUrl: String,
    val eventsUrl: String,
    val receivedEventsUrl: String,
    val type: String,
    val siteAdmin: Boolean
) {
    companion object {
        fun createMockedInstance(): GitHubUser {
            return GitHubUser(
                login = "mocked_login",
                id = 123456,
                nodeId = "mocked_node_id",
                avatarUrl = "https://example.com/avatar.jpg",
                gravatarId = "mocked_gravatar_id",
                url = "https://example.com/user",
                htmlUrl = "https://example.com/user",
                followersUrl = "https://example.com/user/followers",
                followingUrl = "https://example.com/user/following",
                gistsUrl = "https://example.com/user/gists",
                starredUrl = "https://example.com/user/starred",
                subscriptionsUrl = "https://example.com/user/subscriptions",
                organizationsUrl = "https://example.com/user/orgs",
                reposUrl = "https://example.com/user/repos",
                eventsUrl = "https://example.com/user/events",
                receivedEventsUrl = "https://example.com/user/received_events",
                type = "User",
                siteAdmin = false
            )
        }
    }
}