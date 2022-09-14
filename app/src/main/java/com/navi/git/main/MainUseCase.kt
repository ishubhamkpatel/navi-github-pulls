package com.navi.git.main

import android.content.Context
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.map
import com.navi.git.R
import com.navi.git.models.PullRequestUiModel
import com.navi.git.models.toUiModel
import com.navi.logger.Logger
import com.navi.networking.models.ErrorModel
import com.navi.networking.repo.GitRepository
import com.navi.utils.*
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface MainUseCase {
    val navStateFlow: StateFlow<MainNavState>
    val uiStateFlow: StateFlow<MainUiState>
    suspend fun fetchPullRequests()
    suspend fun navigateBack()
    suspend fun retryLoadRequest()
    suspend fun resolveLoadStates(combinedLoadStates: CombinedLoadStates, itemCount: Int)
    suspend fun fetchPullRequestDetails(pullRequest: PullRequestUiModel)
}

@ViewModelScoped
class MainUseCaseImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    private val connectivityStore: ConnectivityStore,
    private val gitRepository: GitRepository
) : MainUseCase {
    private val coroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { _, throwable -> Logger.e(throwable) }
    }
    private val _navStateFlow by lazy { MutableStateFlow<MainNavState>(MainNavState.PullRequests) }
    override val navStateFlow get() = _navStateFlow.asStateFlow()

    private val _uiStateFlow by lazy { MutableStateFlow<MainUiState>(MainUiState.Default) }
    override val uiStateFlow get() = _uiStateFlow.asStateFlow()

    private suspend fun updateState(state: MainUiState) {
        withContext(mainImmediateDispatcher) {
            when (state) {
                MainUiState.RetryListLoading, MainUiState.EmptyList, is MainUiState.List -> {
                    _navStateFlow.value = MainNavState.PullRequests
                }
                is MainUiState.Error -> {
                    _navStateFlow.value = MainNavState.Error
                }
                else -> {
                    // noinspection: do nothing
                }
            }
        }
        withContext(mainDispatcher) {
            _uiStateFlow.value = state
        }
    }

    override suspend fun fetchPullRequests() {
        if (connectivityStore.isNetworkAvailable()) {
            updateState(MainUiState.Loading)
            withContext(ioDispatcher + coroutineExceptionHandler) {
                gitRepository.fetchPullRequests( // executed on IO
                    owner = Constants.REPO_OWNER,
                    repo = Constants.REPO_NAME,
                    state = Constants.PR_STATE,
                    pageSize = Constants.PAGE_SIZE
                ).flowOn(ioDispatcher).map { pagingData ->
                    pagingData.map { it.toUiModel() } // executed in Default
                }.flowOn(defaultDispatcher).collect {
                    updateState(MainUiState.List(pullRequests = it))
                }
            }
        } else {
            updateState(
                MainUiState.Error(
                    title = context.getString(R.string.text_error_no_internet_title),
                    message = context.getString(R.string.text_error_no_internet_message),
                    fallback = MainUiState.Error.Fallback(
                        text = context.getString(R.string.text_btn_retry),
                        action = MainUiEvent.RetryButtonClick
                    )
                )
            )
        }
    }

    override suspend fun navigateBack() {
        _navStateFlow.value = MainNavState.Back
    }

    override suspend fun retryLoadRequest() {
        updateState(MainUiState.RetryListLoading)
    }

    override suspend fun resolveLoadStates(combinedLoadStates: CombinedLoadStates, itemCount: Int) {
        val isListEmpty = (combinedLoadStates.refresh is LoadState.NotLoading) and (itemCount == 0)
        val loadState = combinedLoadStates.source.refresh
        if (isListEmpty) {
            updateState(
                when (loadState) {
                    LoadState.Loading -> MainUiState.Loading
                    is LoadState.Error -> constructError(loadState)
                    else -> MainUiState.EmptyList
                }
            )
        }
    }

    private suspend fun constructError(loadState: LoadState.Error): MainUiState.Error {
        return withContext(defaultDispatcher) {
            val errorModel = loadState.error.message?.parse<ErrorModel>()
            return@withContext MainUiState.Error(
                title = errorModel?.code?.let {
                    context.getString(R.string.text_error_title, it)
                } ?: context.getString(R.string.text_error_unknown_title),
                message = errorModel?.message?.message
                    ?: context.getString(R.string.text_error_unknown_message),
                fallback = MainUiState.Error.Fallback(
                    text = context.getString(R.string.text_btn_retry),
                    action = MainUiEvent.RetryButtonClick
                )
            )
        }
    }

    override suspend fun fetchPullRequestDetails(pullRequest: PullRequestUiModel) {
        Logger.d("Pull request clicked = $pullRequest")
        // TODO: next action after a PR item is clicked
    }
}