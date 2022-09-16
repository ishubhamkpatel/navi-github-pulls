package com.navi.git.features.userrepoinput

import com.navi.git.main.MainNavState

sealed interface UserRepoInputUiState {
    data class Default(val toolbarTitleText: String) : UserRepoInputUiState

    data class PRQuery(val pageHintText: String) : UserRepoInputUiState

    data class Navigation(val mainNavState: MainNavState) : UserRepoInputUiState
}

sealed interface UserRepoInputUiEvent {
    object ToolbarNavBtnClick : UserRepoInputUiEvent

    data class PRQueryTextChange(val text: String?) : UserRepoInputUiEvent

    data class SearchBtnClick(
        val owner: String,
        val repo: String,
        val prState: String
    ) : UserRepoInputUiEvent
}