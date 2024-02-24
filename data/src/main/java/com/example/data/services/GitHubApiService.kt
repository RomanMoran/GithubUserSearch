package com.example.data.services

import com.example.data.models.GitHubSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// Retrofit interface for GitHub API calls
interface GitHubApiService {
    @GET("search/users")
    suspend fun searchUsers(@Query("q") keyword: String): Response<GitHubSearchResponse>
}
