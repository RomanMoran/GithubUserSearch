package com.example.data.source.remote

import com.example.data.models.GitHubUserDto

// Interface for the remote data source, interacting with the GitHub API
interface GitHubUserRemoteDataSource {
    suspend fun searchUsers(keyword: String): List<GitHubUserDto>
}
