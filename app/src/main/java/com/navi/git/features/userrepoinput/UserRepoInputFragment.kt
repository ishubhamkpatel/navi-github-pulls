package com.navi.git.features.userrepoinput

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
import com.navi.git.databinding.FragmentUserRepoInputBinding
import com.navi.git.main.MainViewModel
import com.navi.utils.safeLet
import com.navi.utils.textChanges
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserRepoInputFragment : Fragment() {
    private var _binding: FragmentUserRepoInputBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel by activityViewModels<MainViewModel>()
    private val viewModel by viewModels<UserRepoInputViewModel>()

    /* Lifecycle */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.reportUiEvent(event = UserRepoInputUiEvent.ToolbarNavBtnClick)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserRepoInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachListeners()
        addFlowBindings()
        addSubscriptions()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        detachListeners()
        _binding = null
    }

    /* Operations */

    private fun attachListeners() {
        with(binding) {
            toolbar.imgToolbarBackBtn.setOnClickListener {
                viewModel.reportUiEvent(event = UserRepoInputUiEvent.ToolbarNavBtnClick)
            }
            txtSearchBtn.setOnClickListener {
                safeLet(
                    editTxtRepoOwnerName.text?.toString(),
                    editTxtRepoName.text?.toString(),
                    editTxtPullRequestsState.text?.toString()
                ) {
                    viewModel.reportUiEvent(
                        event = UserRepoInputUiEvent.SearchBtnClick(
                            owner = it[0],
                            repo = it[1],
                            prState = it[2]
                        )
                    )
                }
            }
        }
    }

    private fun addFlowBindings() {
        val prStateTextFlow = binding.editTxtPullRequestsState.textChanges().apply {
                onEach {
                    viewModel.reportUiEvent(
                        event = UserRepoInputUiEvent.PRQueryTextChange(
                            text = it?.toString()
                        )
                    )
                }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            combine(
                binding.editTxtRepoOwnerName.textChanges(),
                binding.editTxtRepoName.textChanges(),
                prStateTextFlow
            ) { _, _, _ ->
                shouldDisableBtn()
            }.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collectLatest {
                toggleBtnUi(it)
            }
        }
    }

    private fun addSubscriptions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { resolveUiState(it) }
        }
    }

    private fun resolveUiState(state: UserRepoInputUiState) {
        when (state) {
            is UserRepoInputUiState.Default -> {
                bindDefaultUi(state)
            }
            is UserRepoInputUiState.PRQuery -> {
                bindPageHintUi(state)
            }
            is UserRepoInputUiState.Navigation -> {
                mainViewModel.escalateNavigation(navigation = state.navigation)
            }
        }
    }

    private fun bindDefaultUi(state: UserRepoInputUiState.Default) {
        with(binding) {
            state.searchUiModel?.let { searchUiModel ->
                editTxtRepoOwnerName.setText(searchUiModel.owner)
                editTxtRepoName.setText(searchUiModel.repo)
                editTxtPullRequestsState.setText(searchUiModel.prState)
            }
            toggleBtnUi(shouldDisableBtn())
            toolbar.txtToolbarTitle.text = state.toolbarTitleText
        }
    }

    private fun bindPageHintUi(state: UserRepoInputUiState.PRQuery) {
        binding.txtUserRepoInput.text = state.pageHintText
    }

    private fun toggleBtnUi(disable: Boolean) {
        with(binding.txtSearchBtn) {
            isEnabled = !disable
            alpha = if (disable) 0.5f else 1f
        }
    }

    private fun shouldDisableBtn(): Boolean {
        return with(binding) {
            editTxtRepoOwnerName.text.isNullOrEmpty() or
                    editTxtRepoName.text.isNullOrEmpty() or
                    editTxtPullRequestsState.text.isNullOrEmpty()
        }
    }

    private fun detachListeners() {
        with(binding) {
            toolbar.imgToolbarBackBtn.setOnClickListener(null)
            txtSearchBtn.setOnClickListener(null)
        }
    }
}