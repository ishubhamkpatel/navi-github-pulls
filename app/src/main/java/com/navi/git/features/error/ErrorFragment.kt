package com.navi.git.features.error

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.navi.git.databinding.FragmentErrorBinding
import com.navi.git.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ErrorFragment : Fragment() {
    private var _binding: FragmentErrorBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel by activityViewModels<MainViewModel>()
    private val viewModel by viewModels<ErrorViewModel>()

    /* Lifecycle */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.reportUiEvent(event = ErrorUiEvent.ToolbarNavBtnClick)
        }
    }

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
        attachListeners()
        addSubscriptions()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        detachListeners()
        _binding = null
    }

    /* Operations */

    private fun attachListeners() {
        binding.toolbar.imgToolbarBackBtn.setOnClickListener {
            viewModel.reportUiEvent(event = ErrorUiEvent.ToolbarNavBtnClick)
        }
    }

    private fun addSubscriptions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { resolveUiState(it) }
        }
    }

    private fun resolveUiState(state: ErrorUiState) {
        when (state) {
            is ErrorUiState.Default -> {
                bindToolbarUi(state.toolbarTitleText)
            }
            is ErrorUiState.Details -> {
                bindToolbarUi(state.toolbarTitleText)
                bindDetailsUi(state)
            }
            is ErrorUiState.Navigation -> {
                mainViewModel.escalateNavigation(navigation = state.navigation)
            }
        }
    }

    private fun bindToolbarUi(toolbarTitleText: String) {
        binding.toolbar.txtToolbarTitle.text = toolbarTitleText
    }

    private fun bindDetailsUi(state: ErrorUiState.Details) {
        val errorUiModel = state.errorUiModel
        with(binding) {
            txtErrorTitle.text = errorUiModel.title
            txtErrorMessage.text = errorUiModel.message
            with(txtFallbackBtn) {
                text = errorUiModel.fallbackBtn
                setOnClickListener {
                    viewModel.reportUiEvent(event = ErrorUiEvent.FallbackBtnClick)
                }
            }
        }
    }

    private fun detachListeners() {
        with(binding) {
            toolbar.imgToolbarBackBtn.setOnClickListener(null)
            txtFallbackBtn.setOnClickListener(null)
        }
    }
}