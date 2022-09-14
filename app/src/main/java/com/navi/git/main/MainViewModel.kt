package com.navi.git.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.navi.git.ConnectivityMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val connectivityMonitor: ConnectivityMonitor,
    private val mainUseCase: MainUseCase
) : ViewModel() {
    val navState: StateFlow<MainNavState> = mainUseCase.navStateFlow
    val uiState: StateFlow<MainUiState> = mainUseCase.uiStateFlow

    init {
        connectivityMonitor.init()
    }

    fun reportUiEvent(event: MainUiEvent) = resolveUiEvent(event)

    private fun resolveUiEvent(event: MainUiEvent) {
        viewModelScope.launch {
            when (event) {
                MainUiEvent.WindowCreated -> {
                    mainUseCase.fetchPullRequests()
                }
                MainUiEvent.ToolbarNavButtonClick -> {
                    mainUseCase.navigateBack()
                }
                MainUiEvent.RetryButtonClick -> {
                    mainUseCase.retryLoadRequest()
                }
                is MainUiEvent.LoadStateChange -> {
                    mainUseCase.resolveLoadStates(event.combinedLoadStates, event.itemCount)
                }
                is MainUiEvent.PullRequestItemClick -> {
                    mainUseCase.fetchPullRequestDetails(event.pullRequest)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        connectivityMonitor.dispose()
    }
}