package com.navi.git.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.navi.git.R
import com.navi.git.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var _navController: NavController? = null
    private val navController
        get() = _navController
            ?: ((supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
                .navController).also { _navController = it }
    private val viewModel by viewModels<MainViewModel>()

    /* Lifecycle */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        displayToolbar()
        addSubscriptions()
        viewModel.reportUiEvent(event = MainUiEvent.WindowCreated)
    }

    /* Operations */

    private fun displayToolbar() {
        binding.toolbar.apply {
            imgToolbarBackBtn.setOnClickListener {
                viewModel.reportUiEvent(event = MainUiEvent.ToolbarNavButtonClick)
            }
            txtToolbarTitle.text = getString(R.string.text_toolbar_closed_prs)
        }
    }

    private fun addSubscriptions() {
        lifecycleScope.launch {
            viewModel.navState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { simulateNavigation(it) }
        }
        lifecycleScope.launch {
            viewModel.uiState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { displayUi(it) }
        }
    }

    private fun simulateNavigation(state: MainNavState) {
        state.id?.let {
            navController.navigate(it)
        } ?: when (state) {
            MainNavState.Back -> onBackPressedDispatcher.onBackPressed()
            else -> {
                // noinspection: do nothing
            }
        }
    }

    private fun displayUi(state: MainUiState) {
        binding.progressBar.isVisible = state is MainUiState.Loading
    }
}