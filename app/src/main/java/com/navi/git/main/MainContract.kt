package com.navi.git.main

import androidx.annotation.IdRes
import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
import com.navi.git.R
import com.navi.git.models.PullRequestUiModel

sealed interface MainUiState {
    object Default : MainUiState
    object Loading : MainUiState
    object RetryListLoading : MainUiState
    object EmptyList : MainUiState

    data class List(val pullRequests: PagingData<PullRequestUiModel>) : MainUiState

    data class Error(
        val title: String,
        val message: String,
        val fallback: Fallback? = null
    ) : MainUiState {
        data class Fallback(val text: String, val action: MainUiEvent)
    }
}

sealed interface MainUiEvent {
    object WindowCreated : MainUiEvent
    object ToolbarNavButtonClick : MainUiEvent
    object RetryButtonClick : MainUiEvent

    data class LoadStateChange(
        val combinedLoadStates: CombinedLoadStates,
        val itemCount: Int
    ) : MainUiEvent

    data class PullRequestItemClick(val pullRequest: PullRequestUiModel) : MainUiEvent
}

sealed class MainNavState(@IdRes val id: Int?) {
    object Back : MainNavState(null)
    object PullRequests : MainNavState(R.id.action_pull_requests_fragment)
    object Error : MainNavState(R.id.action_error_fragment)
}