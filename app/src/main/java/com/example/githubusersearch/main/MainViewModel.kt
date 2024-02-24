package com.example.githubusersearch.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.GitHubUser
import com.example.domain.usecases.SearchUsersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val searchUsersUseCase: SearchUsersUseCase) : ViewModel() {

    // UI State represented as a StateFlow to emit updates to the UI layer
    private val _uiState = MutableStateFlow<UiState>(UiState.Empty)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // Function to initiate user search
    fun searchUsers(keyword: String) {
        // Avoid searching for empty queries
        if (keyword.isBlank()) {
            _uiState.value = UiState.Empty
            return
        }

        _uiState.value = UiState.Loading

        viewModelScope.launch {
            searchUsersUseCase(keyword).collect { result ->

                _uiState.value = when  {
                    result.isSuccess -> UiState.Success(result.getOrThrow())
                    else -> UiState.Error(result.exceptionOrNull()?.message ?: "An error occurred")
                }
            }
        }
    }

    // UI State sealed class to handle different UI states
    sealed class UiState {
        object Loading : UiState()
        object Empty : UiState()
        data class Success(val users: List<GitHubUser>) : UiState()
        data class Error(val message: String) : UiState()
    }
}