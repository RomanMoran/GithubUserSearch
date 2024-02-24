package com.example.domain.usecases

import com.example.domain.models.GitHubUser
import com.example.domain.repositories.GitHubUserRepository
import kotlinx.coroutines.flow.Flow

// Use case for searching GitHub users
class SearchUsersUseCase(private val gitHubUserRepository: GitHubUserRepository) {

    // todo understand for what purpose here 'operator'
    operator fun invoke(keyword: String): Flow<Result<List<GitHubUser>>> =
        gitHubUserRepository.searchUsers(keyword)

}
