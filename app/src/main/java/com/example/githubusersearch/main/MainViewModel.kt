package com.example.githubusersearch.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.GitHubUser
import com.example.domain.usecases.SearchUsersUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(private val searchUsersUseCase: SearchUsersUseCase) : ViewModel() {

    // UI State represented as a StateFlow to emit updates to the UI layer
    private val _uiState = MutableStateFlow<UiState>(UiState.Empty)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val searchQuery = MutableStateFlow("")

    init {
        viewModelScope.launch {
            searchQuery
                .onEach { query ->
                    if (query.isEmpty()) {
                        _uiState.value = UiState.Empty
                    }
                }
                .debounce(300L)
                .distinctUntilChanged() // Ignore repeated sequential values
                .flatMapLatest { query ->
                    if (query.isEmpty()) {
                        // Возвращаем flow, который немедленно завершается для пустого запроса
                        flowOf(UiState.Empty)
                    } else {
                        searchUsersUseCase(query)
                            .map { result ->
                                when {
                                    result.isSuccess -> UiState.Success(result.getOrThrow())
                                    result.isFailure -> UiState.Error(
                                        result.exceptionOrNull()?.message ?: "An error occurred"
                                    )

                                    else -> UiState.Loading // Эта ветка скорее всего не будет достигнута
                                }
                            }
                            .onStart { emit(UiState.Loading) } // Показываем UI состояние загрузки перед запросом
                    }
                }
                .catch { e ->
                    if (e !is CancellationException) {
                        _uiState.value = UiState.Error(e.message ?: "An unexpected error occurred")
                    }
                }
                .collect { result ->
                    // Эта логика теперь в flatMapLatest
                    _uiState.value = result
                }
        }
    }

    // Function to initiate user search
    fun searchUsers(query: String) {
        _uiState.value = UiState.Loading
        searchQuery.value = query
    }

    // UI State sealed class to handle different UI states
    sealed class UiState {
        object Loading : UiState()
        object Empty : UiState()
        data class Success(val users: List<GitHubUser>) : UiState()
        data class Error(val message: String) : UiState()
    }
}