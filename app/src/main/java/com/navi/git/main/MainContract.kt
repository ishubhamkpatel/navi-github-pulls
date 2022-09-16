package com.navi.git.main

import androidx.navigation.NavDirections
import com.navi.git.NavGraphMainDirections
import com.navi.git.models.ErrorUiModel
import com.navi.git.models.SearchUiModel

sealed class MainNavigation(val navDirections: NavDirections? = null) {
    object Back : MainNavigation()

    data class UserRepoInput(
        val searchUiModel: SearchUiModel
    ) : MainNavigation(
        NavGraphMainDirections.actionUserRepoInputFragment(searchUiModel)
    )

    data class PullRequests(
        val searchUiModel: SearchUiModel
    ) : MainNavigation(
        NavGraphMainDirections.actionPullRequestsFragment(searchUiModel)
    )

    data class Error(
        val errorUiModel: ErrorUiModel,
        val searchUiModel: SearchUiModel
    ) : MainNavigation(
        NavGraphMainDirections.actionErrorFragment(searchUiModel, errorUiModel)
    )
}