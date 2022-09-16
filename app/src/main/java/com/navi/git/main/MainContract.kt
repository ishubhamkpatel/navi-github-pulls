package com.navi.git.main

import androidx.annotation.IdRes
import com.navi.git.R

sealed class MainNavState(@IdRes val id: Int?) {
    object Back : MainNavState(null)

    object UserRepoInput : MainNavState(R.id.action_user_repo_input_fragment)

    object PullRequests : MainNavState(R.id.action_pull_requests_fragment)

    object Error : MainNavState(R.id.action_error_fragment)
}