package com.example.data.source.remote

import com.example.data.models.GitHubUserDto
import com.example.data.services.GitHubApiService

class GitHubUserRemoteDataSourceImpl(private val apiService: GitHubApiService) :
    GitHubUserRemoteDataSource {

    override suspend fun searchUsers(keyword: String): List<GitHubUserDto> {
        // Handle the API call
        val response = apiService.searchUsers(keyword)
        if (response.isSuccessful && response.body() != null) {
            // Convert and return the list of user DTOs
            return response.body()!!.items
        } else {
            // Handle errors or empty cases appropriately
            throw Exception("Failed to fetch users: ${response.message()}")
        }
    }
}
