package com.navi.git.features.error

import com.navi.git.main.MainNavigation
import com.navi.git.models.ErrorUiModel

sealed interface ErrorUiState {
    data class Default(val toolbarTitleText: String) : ErrorUiState

    data class Details(val toolbarTitleText: String, val errorUiModel: ErrorUiModel) : ErrorUiState

    data class Navigation(val navigation: MainNavigation) : ErrorUiState
}

sealed interface ErrorUiEvent {
    object ToolbarNavBtnClick : ErrorUiEvent

    object FallbackBtnClick : ErrorUiEvent
}