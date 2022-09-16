package com.navi.git.features.userrepoinput

import com.navi.git.main.MainNavigation
import com.navi.git.models.SearchUiModel

sealed interface UserRepoInputUiState {
    data class Default(
        val toolbarTitleText: String,
        val searchUiModel: SearchUiModel? = null
    ) : UserRepoInputUiState

    data class PRQuery(val pageHintText: String) : UserRepoInputUiState

    data class Navigation(val navigation: MainNavigation) : UserRepoInputUiState
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