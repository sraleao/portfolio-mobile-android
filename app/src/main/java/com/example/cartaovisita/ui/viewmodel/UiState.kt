package com.example.cartaovisita.ui.viewmodel

sealed class UiState {
    object Loading : UiState()
    data class Success(val projects: List<com.example.cartaovisita.domain.Project>) : UiState()
    data class Error(val message: String) : UiState()
}
