package com.example.githubusersearch.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.AnimationDuration
import com.example.domain.usecases.animation_duration.AnimationDurationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(private val animationDurationUseCase: AnimationDurationUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Empty)
    val uiState : StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = UiState.Success(animationDurationUseCase.getAnimationDuration())
        }
    }

    // UI State sealed class to handle different UI states
    sealed class UiState {
        object Loading : UiState()
        object Empty : UiState()
        data class Success(val duration: AnimationDuration) : UiState()
        data class Error(val message: String) : UiState()
    }

}