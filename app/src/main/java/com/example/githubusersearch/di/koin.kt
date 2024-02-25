package com.example.githubusersearch.di

import com.example.data.repositories.GitHubUserRepositoryImpl
import com.example.data.services.GitHubApiService
import com.example.data.source.remote.GitHubUserRemoteDataSource
import com.example.data.source.remote.GitHubUserRemoteDataSourceImpl
import com.example.domain.repositories.GitHubUserRepository
import com.example.domain.usecases.SearchUsersUseCase
import com.example.domain.usecases.SearchUsersUseCaseImpl
import com.example.githubusersearch.BuildConfig
import com.example.githubusersearch.main.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single { provideOkHttpClient() }
    single { provideRetrofit(get()) }
    single<GitHubApiService> { get<Retrofit>().create(GitHubApiService::class.java) }
    single<GitHubUserRemoteDataSource> { GitHubUserRemoteDataSourceImpl(apiService = get()) }
    single<GitHubUserRepository> { GitHubUserRepositoryImpl(remoteDataSource = get()) }
    single<SearchUsersUseCase> { SearchUsersUseCaseImpl(gitHubUserRepository = get()) }
    viewModel { MainViewModel(searchUsersUseCase = get()) }
    // Add other dependencies
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
}

fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("Authorization", "Bearer ${BuildConfig.GITHUB_API_TOKEN}")
                .header("Accept", "application/vnd.github+json")
                .header("X-GitHub-Api-Version", "2022-11-28")
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        .addNetworkInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        })
        .build()
}
