package com.navi.networking

import com.navi.networking.models.data.PullRequestDataModel
import dagger.Reusable
import javax.inject.Inject

internal interface ApiManager {
    suspend fun fetchPullRequests(
        owner: String,
        repo: String,
        state: String,
        page: Int
    ): ApiResponse<List<PullRequestDataModel>>
}

@Reusable
internal class ApiManagerImpl @Inject constructor(private val api: Api) : ApiManager {
    override suspend fun fetchPullRequests(
        owner: String,
        repo: String,
        state: String,
        page: Int
    ): ApiResponse<List<PullRequestDataModel>> = backOffCall {
        api.getPullRequests(owner = owner, repo = repo, state = state, page = page)
    }
}