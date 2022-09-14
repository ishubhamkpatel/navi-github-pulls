package com.navi.git.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.navi.git.databinding.FragmentErrorBinding
import com.navi.git.main.MainUiState
import com.navi.git.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ErrorFragment : Fragment() {
    private var _binding: FragmentErrorBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<MainViewModel>()

    /* Lifecycle */

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentErrorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addSubscriptions()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /* Operations */

    private fun addSubscriptions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { resolveUiState(it) }
        }
    }

    private fun resolveUiState(state: MainUiState) {
        binding.root.isVisible = state is MainUiState.Error
        when (state) {
            is MainUiState.Error -> bindErrorUi(state)
            else -> {
                // noinspection: do nothing
            }
        }
    }

    private fun bindErrorUi(state: MainUiState.Error) {
        binding.apply {
            txtErrorTitle.text = state.title
            txtErrorMessage.text = state.message
            state.fallback?.let { fallback ->
                txtFallbackBtn.apply {
                    isVisible = true
                    text = fallback.text
                    setOnClickListener { viewModel.reportUiEvent(event = fallback.action) }
                }
            } ?: kotlin.run {
                txtFallbackBtn.isVisible = true
            }
        }
    }
}