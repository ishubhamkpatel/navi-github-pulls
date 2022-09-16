package com.navi.git.features.pullrequests

import androidx.paging.CombinedLoadStates
import com.navi.git.main.MainNavigation
import com.navi.git.models.PullRequestUiModel

sealed interface PullRequestsUiState {
    data class Default(val toolbarTitleText: String) : PullRequestsUiState

    object Loading : PullRequestsUiState

    object List : PullRequestsUiState

    data class Empty(val pageText: String) : PullRequestsUiState

    data class Navigation(val navigation: MainNavigation) : PullRequestsUiState
}

sealed interface PullRequestsUiEvent {
    object ToolbarNavBtnClick : PullRequestsUiEvent

    data class LoadStateChange(
        val combinedLoadStates: CombinedLoadStates,
        val itemCount: Int
    ) : PullRequestsUiEvent

    data class ItemClick(val pullRequest: PullRequestUiModel) : PullRequestsUiEvent
}