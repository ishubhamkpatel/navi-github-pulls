package com.navi.git.features.error

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.navi.git.models.ErrorUiModel
import com.navi.git.models.SearchUiModel
import com.navi.git.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ErrorViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val errorUseCase: ErrorUseCase
) : ViewModel() {
    val uiState by lazy { errorUseCase.uiStateFlow }

    init {
        viewModelScope.launch {
            errorUseCase.setArgs(
                searchUiModel = savedStateHandle.get<SearchUiModel>(
                    Constants.NAV_KEY_SEARCH_UI_MODEL
                ) ?: SearchUiModel.default,
                errorUiModel = savedStateHandle.get<ErrorUiModel>(
                    Constants.NAV_KEY_ERROR_UI_MODEL
                ) ?: ErrorUiModel.default
            )
        }
    }

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