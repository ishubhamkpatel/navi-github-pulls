package com.navi.git.features.pullrequests

import android.content.Context
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.map
import com.navi.git.R
import com.navi.git.main.MainNavState
import com.navi.git.models.PullRequestUiModel
import com.navi.git.models.toUiModel
import com.navi.logger.Logger
import com.navi.networking.models.ErrorModel
import com.navi.networking.repo.GitRepository
import com.navi.utils.DefaultDispatcher
import com.navi.utils.IoDispatcher
import com.navi.utils.parse
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface PullRequestsUseCase {
    val uiStateFlow: StateFlow<PullRequestsUiState>
    fun pullRequestsFlow(): Flow<PagingData<PullRequestUiModel>>

    suspend fun onToolbarNavBtnClicked()
    suspend fun onLoadStateChanged(combinedLoadStates: CombinedLoadStates, itemCount: Int)
    suspend fun onItemClicked(pullRequest: PullRequestUiModel)
}

@ViewModelScoped
class PullRequestsUseCaseImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val gitRepository: GitRepository
) : PullRequestsUseCase {
    companion object {
        private const val PAGE_SIZE = 25
    }

    private val _uiStateFlow by lazy {
        MutableStateFlow<PullRequestsUiState>(
            PullRequestsUiState.Default(
                toolbarTitleText = context.getString(R.string.text_toolbar_github_repo, "")
            )
        )
    }
    override val uiStateFlow: StateFlow<PullRequestsUiState>
        get() = _uiStateFlow.asStateFlow()

    override fun pullRequestsFlow(): Flow<PagingData<PullRequestUiModel>> {
        return gitRepository.pullRequestsFlow(
            owner = "octocat",
            repo = "Hello-World",
            state = "closed",
            pageSize = PAGE_SIZE
        ).flowOn(ioDispatcher).map { pagingData ->
            pagingData.map { it.toUiModel() }
        }.flowOn(defaultDispatcher)
    }

    override suspend fun onToolbarNavBtnClicked() {
        _uiStateFlow.value = PullRequestsUiState.Navigation(
            mainNavState = MainNavState.UserRepoInput
        )
    }

    override suspend fun onLoadStateChanged(
        combinedLoadStates: CombinedLoadStates,
        itemCount: Int
    ) {
        val isListEmpty = (combinedLoadStates.refresh is LoadState.NotLoading) and (itemCount == 0)
        val loadState = combinedLoadStates.source.refresh
        _uiStateFlow.value = if (isListEmpty) {
            when (loadState) {
                LoadState.Loading -> PullRequestsUiState.Loading
                is LoadState.Error -> {
                    PullRequestsUiState.Navigation(
                        mainNavState = MainNavState.Error
                    )
                }
                else -> PullRequestsUiState.Empty(
                    pageText = context.getString(R.string.text_pull_requests_empty)
                )
            }
        } else {
            PullRequestsUiState.List
        }
    }

    private suspend fun constructError(
        loadState: LoadState.Error
    ) = withContext(defaultDispatcher) {
        loadState.error.message?.parse<ErrorModel>()
    }

    override suspend fun onItemClicked(pullRequest: PullRequestUiModel) {
        Logger.d("Pull request clicked = $pullRequest")
        // TODO: next action after a PR item is clicked
    }
}