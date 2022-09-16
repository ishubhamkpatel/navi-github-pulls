package com.navi.git.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.navi.git.databinding.ActivityMainBinding
import com.navi.git.utils.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<MainViewModel>()

    /* Lifecycle */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        addSubscriptions()
    }

    /* Operations */

    private fun addSubscriptions() {
        lifecycleScope.launch {
            viewModel.navState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { simulateNavigation(it) }
        }
    }

    private fun simulateNavigation(state: MainNavState) {
        state.id?.let {
            findNavController(binding.navHostFragment.id).safeNavigate(it)
        } ?: when (state) {
            MainNavState.Back -> onBackPressedDispatcher.onBackPressed()
            else -> {
                // noinspection: do nothing
            }
        }
    }
}