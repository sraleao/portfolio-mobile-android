package com.example.cartaovisita.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cartaovisita.repository.ProjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProjectListViewModel(
    private val repository: ProjectRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {

        // Coleta contínua do banco
        viewModelScope.launch {
            repository.getProjectsFlow().collectLatest { projects ->
                _uiState.value = UiState.Success(projects)
            }
        }

        // Sincroniza com a API
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = repository.syncFromNetwork()

            if (result.isFailure) {
                _uiState.value = UiState.Error(
                    result.exceptionOrNull()?.localizedMessage ?: "Erro ao atualizar projetos"
                )
            }
        }
    }
}
