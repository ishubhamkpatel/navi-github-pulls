package com.navi.git.features.error

import android.content.Context
import com.navi.git.R
import com.navi.git.main.MainNavigation
import com.navi.git.models.ErrorUiModel
import com.navi.git.models.SearchUiModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface ErrorUseCase {
    val uiStateFlow: StateFlow<ErrorUiState>

    suspend fun setArgs(searchUiModel: SearchUiModel, errorUiModel: ErrorUiModel)
    suspend fun onToolbarNavBtnClicked()
    suspend fun onFallbackBtnClicked()
}

@ViewModelScoped
class ErrorUseCaseImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ErrorUseCase {
    private lateinit var searchUiModel: SearchUiModel

    private val _uiStateFlow by lazy {
        MutableStateFlow<ErrorUiState>(
            ErrorUiState.Default(
                toolbarTitleText = context.getString(R.string.text_toolbar_github_repo, "")
            )
        )
    }
    override val uiStateFlow: StateFlow<ErrorUiState>
        get() = _uiStateFlow.asStateFlow()

    override suspend fun setArgs(searchUiModel: SearchUiModel, errorUiModel: ErrorUiModel) {
        this.searchUiModel = searchUiModel
        _uiStateFlow.value = ErrorUiState.Details(errorUiModel = errorUiModel)
    }

    override suspend fun onToolbarNavBtnClicked() {
        _uiStateFlow.value = ErrorUiState.Navigation(
            navigation = MainNavigation.UserRepoInput(searchUiModel = searchUiModel)
        )
    }

    override suspend fun onFallbackBtnClicked() {
        _uiStateFlow.value = ErrorUiState.Navigation(
            navigation = MainNavigation.PullRequests(searchUiModel = searchUiModel)
        )
    }
}