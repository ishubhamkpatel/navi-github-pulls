package com.navi.networking

import com.navi.networking.models.data.PullRequestDataModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface Api {
    @GET("/repos/{owner}/{repo}/pulls")
    suspend fun getPullRequests(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Query("state") state: String,
        @Query("page") page: Int
    ): Response<List<PullRequestDataModel>>
}