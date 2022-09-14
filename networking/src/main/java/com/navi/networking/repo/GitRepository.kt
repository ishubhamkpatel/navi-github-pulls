package com.navi.networking.repo

import androidx.paging.*
import com.navi.networking.ApiManager
import com.navi.networking.ApiResponse
import com.navi.networking.models.data.PullRequestDataModel
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GitRepository {
    suspend fun fetchPullRequests(
        owner: String,
        repo: String,
        state: String,
        pageSize: Int
    ): Flow<PagingData<PullRequestDataModel>>
}

@Reusable
internal class GitRepositoryImpl @Inject constructor(
    private val apiManager: ApiManager
) : GitRepository {
    companion object {
        private const val GIT_PR_PAGE_INDEX = 1
    }

    override suspend fun fetchPullRequests(
        owner: String,
        repo: String,
        state: String,
        pageSize: Int
    ): Flow<PagingData<PullRequestDataModel>> = Pager(
        config = PagingConfig(pageSize = pageSize, enablePlaceholders = false),
        pagingSourceFactory = { PullRequestsPagingSource(apiManager, owner, repo, state, pageSize) }
    ).flow

    private inner class PullRequestsPagingSource(
        private val apiManager: ApiManager,
        private val owner: String,
        private val repo: String,
        private val state: String,
        private val pageSize: Int
    ) : PagingSource<Int, PullRequestDataModel>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PullRequestDataModel> {
            val pageIndex = params.key ?: GIT_PR_PAGE_INDEX
            return when (val apiResponse = apiManager.fetchPullRequests(
                owner = owner, repo = repo, state = state, page = pageIndex
            )) {
                is ApiResponse.Success -> {
                    val pullRequests = apiResponse.data
                    LoadResult.Page(
                        data = pullRequests,
                        prevKey = if (pageIndex == GIT_PR_PAGE_INDEX) null else pageIndex,
                        nextKey = if (pullRequests.isEmpty()) null else pageIndex + (params.loadSize / pageSize)
                    )
                }
                is ApiResponse.Error -> {
                    LoadResult.Error(apiResponse.throwable)
                }
            }
        }

        override fun getRefreshKey(state: PagingState<Int, PullRequestDataModel>): Int? {
            return state.anchorPosition?.let {
                state.closestPageToPosition(it)?.prevKey?.plus(1)
                    ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
            }
        }
    }
}