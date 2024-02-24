package com.example.domain.repositories

import com.example.domain.models.GitHubUser
import kotlinx.coroutines.flow.Flow

interface GitHubUserRepository {

    fun searchUsers(keyword: String): Flow<Result<List<GitHubUser>>>

}
