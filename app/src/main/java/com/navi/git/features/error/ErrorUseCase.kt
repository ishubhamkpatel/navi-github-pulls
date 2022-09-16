package com.navi.git.features.error

import android.content.Context
import com.navi.git.R
import com.navi.git.main.MainNavState
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface ErrorUseCase {
    val uiStateFlow: StateFlow<ErrorUiState>

    suspend fun onToolbarNavBtnClicked()
    suspend fun onFallbackBtnClicked()
}

@ViewModelScoped
class ErrorUseCaseImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ErrorUseCase {
    private val _uiStateFlow by lazy {
        MutableStateFlow<ErrorUiState>(
            ErrorUiState.Default(
                toolbarTitleText = context.getString(R.string.text_toolbar_github_repo, "")
            )
        )
    }
    override val uiStateFlow: StateFlow<ErrorUiState>
        get() = _uiStateFlow.asStateFlow()

    override suspend fun onToolbarNavBtnClicked() {
        _uiStateFlow.value = ErrorUiState.Navigation(mainNavState = MainNavState.UserRepoInput)
    }

    override suspend fun onFallbackBtnClicked() {
        _uiStateFlow.value = ErrorUiState.Navigation(mainNavState = MainNavState.PullRequests)
    }
}