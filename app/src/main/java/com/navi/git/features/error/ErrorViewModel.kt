package com.navi.git.features.error

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ErrorViewModel @Inject constructor(private val errorUseCase: ErrorUseCase) : ViewModel() {
    val uiState by lazy { errorUseCase.uiStateFlow }

    fun reportUiEvent(event: ErrorUiEvent) = resolveUiEvent(event)

    private fun resolveUiEvent(event: ErrorUiEvent) {
        viewModelScope.launch {
            when (event) {
                ErrorUiEvent.ToolbarNavBtnClick -> {
                    errorUseCase.onToolbarNavBtnClicked()
                }
                is ErrorUiEvent.FallbackBtnClick -> {
                    errorUseCase.onFallbackBtnClicked()
                }
            }
        }
    }
}