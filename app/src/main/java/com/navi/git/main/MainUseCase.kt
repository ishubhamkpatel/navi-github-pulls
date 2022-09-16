package com.navi.git.main

import com.navi.git.models.SearchUiModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface MainUseCase {
    val navStateFlow: StateFlow<MainNavigation>

    suspend fun navigate(navigation: MainNavigation)
}

@ViewModelScoped
class MainUseCaseImpl @Inject constructor() : MainUseCase {
    private val _navStateFlow by lazy {
        MutableStateFlow<MainNavigation>(
            MainNavigation.UserRepoInput(
                searchUiModel = SearchUiModel.default
            )
        )
    }
    override val navStateFlow get() = _navStateFlow.asStateFlow()

    override suspend fun navigate(navigation: MainNavigation) {
        _navStateFlow.value = navigation
    }
}