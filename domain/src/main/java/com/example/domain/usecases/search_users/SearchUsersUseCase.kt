package com.example.domain.usecases.search_users

import com.example.domain.models.GitHubUser
import kotlinx.coroutines.flow.Flow

interface SearchUsersUseCase {

    operator fun invoke(keyword: String): Flow<Result<List<GitHubUser>>>

}