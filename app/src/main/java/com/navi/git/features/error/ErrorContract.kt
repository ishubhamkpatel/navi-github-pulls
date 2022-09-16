package com.navi.git.features.error

import com.navi.git.main.MainNavState

sealed interface ErrorUiState {
    data class Default(val toolbarTitleText: String) : ErrorUiState

    data class Details(
        val titleText: String,
        val messageText: String,
        val fallbackBtnText: String
    ) : ErrorUiState

    data class Navigation(val mainNavState: MainNavState) : ErrorUiState
}

sealed interface ErrorUiEvent {
    object ToolbarNavBtnClick : ErrorUiEvent

    object FallbackBtnClick : ErrorUiEvent
}