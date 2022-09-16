package com.navi.git.features.pullrequests

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.navi.git.models.SearchUiModel
import com.navi.git.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PullRequestsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val pullRequestsUseCase: PullRequestsUseCase
) : ViewModel() {
    val uiState by lazy { pullRequestsUseCase.uiStateFlow }

    init {
        viewModelScope.launch {
            pullRequestsUseCase.setArgs(
                searchUiModel = savedStateHandle.get<SearchUiModel>(
                    Constants.NAV_KEY_SEARCH_UI_MODEL
                ) ?: SearchUiModel.default
            )
        }
    }

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