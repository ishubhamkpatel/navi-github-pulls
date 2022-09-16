package com.navi.git.features.userrepoinput

import android.content.Context
import com.navi.git.R
import com.navi.git.main.MainNavState
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface UserRepoInputUseCase {
    val uiStateFlow: StateFlow<UserRepoInputUiState>

    suspend fun onToolbarNavBtnClicked()
    suspend fun onPRQueryTextChanged(text: String?)
    suspend fun onSearchBtnClicked(owner: String, repo: String, prState: String)
}

@ViewModelScoped
class UserRepoInputUseCaseImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : UserRepoInputUseCase {
    private val _uiStateFlow by lazy {
        MutableStateFlow<UserRepoInputUiState>(
            UserRepoInputUiState.Default(
                toolbarTitleText = context.getString(
                    R.string.text_toolbar_github_repo,
                    ""
                )
            )
        )
    }
    override val uiStateFlow: StateFlow<UserRepoInputUiState>
        get() = _uiStateFlow.asStateFlow()

    override suspend fun onToolbarNavBtnClicked() {
        _uiStateFlow.value = UserRepoInputUiState.Navigation(mainNavState = MainNavState.Back)
    }

    override suspend fun onPRQueryTextChanged(text: String?) {
        val formatArg = text?.let { "$it " } ?: ""
        _uiStateFlow.value = UserRepoInputUiState.PRQuery(
            pageHintText = context.getString(
                R.string.text_user_repo_input,
                formatArg
            )
        )
    }

    override suspend fun onSearchBtnClicked(owner: String, repo: String, prState: String) {
        _uiStateFlow.value = UserRepoInputUiState.Navigation(
            mainNavState = MainNavState.PullRequests
        )
    }
}