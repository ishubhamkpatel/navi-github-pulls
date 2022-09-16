package com.navi.git.features.pullrequests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PullRequestsViewModel @Inject constructor(
    private val pullRequestsUseCase: PullRequestsUseCase
) : ViewModel() {
    val uiState by lazy { pullRequestsUseCase.uiStateFlow }

    fun pullRequestsFlow() = pullRequestsUseCase.pullRequestsFlow().cachedIn(viewModelScope)

    fun reportUiEvent(event: PullRequestsUiEvent) = resolveUiEvent(event)

    private fun resolveUiEvent(event: PullRequestsUiEvent) {
        viewModelScope.launch {
            when (event) {
                PullRequestsUiEvent.ToolbarNavBtnClick -> {
                    pullRequestsUseCase.onToolbarNavBtnClicked()
                }
                is PullRequestsUiEvent.LoadStateChange -> {
                    pullRequestsUseCase.onLoadStateChanged(
                        combinedLoadStates = event.combinedLoadStates,
                        itemCount = event.itemCount
                    )
                }
                is PullRequestsUiEvent.ItemClick -> {
                    pullRequestsUseCase.onItemClicked(pullRequest = event.pullRequest)
                }
            }
        }
    }
}