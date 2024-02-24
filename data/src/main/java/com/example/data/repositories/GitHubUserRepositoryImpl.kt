package com.example.data.repositories

import com.example.data.source.remote.GitHubUserRemoteDataSource
import com.example.data.models.GitHubUserDto
import com.example.domain.models.GitHubUser
import com.example.domain.repositories.GitHubUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GitHubUserRepositoryImpl(
    private val remoteDataSource: GitHubUserRemoteDataSource
) : GitHubUserRepository {
    override fun searchUsers(keyword: String): Flow<Result<List<GitHubUser>>> = flow {
        try {
            val users = remoteDataSource.searchUsers(keyword)
            emit(Result.success(users.map { toDomain(it) }))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    // todo create mapper class
    fun toDomain(dto: GitHubUserDto): GitHubUser {
        return GitHubUser(
            login = dto.login,
            id = dto.id,
            nodeId = dto.nodeId,
            avatarUrl = dto.avatarUrl,
            gravatarId = dto.gravatarId,
            url = dto.url,
            htmlUrl = dto.htmlUrl,
            followersUrl = dto.followersUrl,
            followingUrl = dto.followingUrl,
            gistsUrl = dto.gistsUrl,
            starredUrl = dto.starredUrl,
            subscriptionsUrl = dto.subscriptionsUrl,
            organizationsUrl = dto.organizationsUrl,
            reposUrl = dto.reposUrl,
            eventsUrl = dto.eventsUrl,
            receivedEventsUrl = dto.receivedEventsUrl,
            type = dto.type,
            siteAdmin = dto.siteAdmin
        )
    }

}

