package com.navi.git.features.userrepoinput

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.navi.git.models.SearchUiModel
import com.navi.git.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserRepoInputViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userRepoInputUseCase: UserRepoInputUseCase
) : ViewModel() {
    val uiState by lazy { userRepoInputUseCase.uiStateFlow }

    init {
        viewModelScope.launch {
            savedStateHandle.get<SearchUiModel>(Constants.NAV_KEY_SEARCH_UI_MODEL)?.let {
                userRepoInputUseCase.setArgs(searchUiModel = it)
            }
        }
    }

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
                    val searchUiModel = SearchUiModel(
                        owner = event.owner,
                        repo = event.repo,
                        prState = event.prState
                    )
                    savedStateHandle[Constants.NAV_KEY_SEARCH_UI_MODEL] = searchUiModel
                    userRepoInputUseCase.onSearchBtnClicked(searchUiModel = searchUiModel)
                }
            }
        }
    }
}