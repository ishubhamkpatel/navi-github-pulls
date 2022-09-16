package com.navi.git.main

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface MainUseCase {
    val navStateFlow: StateFlow<MainNavState>

    suspend fun reportNavState(state: MainNavState)
}

@ViewModelScoped
class MainUseCaseImpl @Inject constructor() : MainUseCase {
    private val _navStateFlow by lazy { MutableStateFlow<MainNavState>(MainNavState.UserRepoInput) }
    override val navStateFlow get() = _navStateFlow.asStateFlow()

    override suspend fun reportNavState(state: MainNavState) {
        _navStateFlow.value = state
    }
}