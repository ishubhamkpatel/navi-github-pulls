package com.navi.git.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.navi.git.utils.ConnectivityMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    connectivityMonitor: ConnectivityMonitor,
    private val mainUseCase: MainUseCase
) : ViewModel(connectivityMonitor) {
    val navState by lazy { mainUseCase.navStateFlow }

    init {
        connectivityMonitor.init()
    }

    fun escalateNavState(state: MainNavState) {
        viewModelScope.launch {
            mainUseCase.reportNavState(state = state)
        }
    }
}