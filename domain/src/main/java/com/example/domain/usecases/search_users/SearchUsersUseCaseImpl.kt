package com.example.domain.usecases.search_users

import com.example.domain.models.GitHubUser
import com.example.domain.repositories.GitHubUserRepository
import kotlinx.coroutines.flow.Flow

// Use case for searching GitHub users
class SearchUsersUseCaseImpl(private val gitHubUserRepository: GitHubUserRepository) :
    SearchUsersUseCase {

    override operator fun invoke(keyword: String): Flow<Result<List<GitHubUser>>> =
        gitHubUserRepository.searchUsers(keyword)

}
