package com.navi.git.features.userrepoinput

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserRepoInputViewModel @Inject constructor(
    private val userRepoInputUseCase: UserRepoInputUseCase
) : ViewModel() {
    val uiState by lazy { userRepoInputUseCase.uiStateFlow }

    fun reportUiEvent(event: UserRepoInputUiEvent) = resolveUiEvent(event)

    private fun resolveUiEvent(event: UserRepoInputUiEvent) {
        viewModelScope.launch {
            when (event) {
                UserRepoInputUiEvent.ToolbarNavBtnClick -> {
                    userRepoInputUseCase.onToolbarNavBtnClicked()
                }
                is UserRepoInputUiEvent.PRQueryTextChange -> {
                    userRepoInputUseCase.onPRQueryTextChanged(text = event.text)
                }
                is UserRepoInputUiEvent.SearchBtnClick -> {
                    userRepoInputUseCase.onSearchBtnClicked(
                        owner = event.owner,
                        repo = event.repo,
                        prState = event.prState
                    )
                }
            }
        }
    }
}