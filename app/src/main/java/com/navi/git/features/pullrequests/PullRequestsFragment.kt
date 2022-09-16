package com.navi.git.features.pullrequests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.recyclerview.widget.LinearLayoutManager
import com.navi.git.databinding.FragmentPullRequestsBinding
import com.navi.git.main.MainViewModel
import com.navi.git.features.pullrequests.adapter.PullRequestsLoadStateAdapter
import com.navi.git.features.pullrequests.adapter.PullRequestsPagedListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PullRequestsFragment : Fragment() {
    private var _binding: FragmentPullRequestsBinding? = null
    private val binding get() = _binding!!
    private lateinit var listAdapter: PullRequestsPagedListAdapter
    private val retryLoadListener: () -> Unit by lazy {
        { listAdapter.retry() }
    }
    private val loadStateListener: (CombinedLoadStates) -> Unit by lazy {
        {
            viewModel.reportUiEvent(
                event = PullRequestsUiEvent.LoadStateChange(
                    combinedLoadStates = it,
                    itemCount = listAdapter.itemCount
                )
            )
        }
    }

    private val mainViewModel by activityViewModels<MainViewModel>()
    private val viewModel by viewModels<PullRequestsViewModel>()

    /* Lifecycle */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.reportUiEvent(event = PullRequestsUiEvent.ToolbarNavBtnClick)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPullRequestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureRecycler()
        attachListeners()
        addSubscriptions()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        detachListeners()
        _binding = null
    }

    /* Operations */

    private fun configureRecycler() {
        with(binding.listPullRequests) {
            layoutManager = LinearLayoutManager(context)
            adapter = PullRequestsPagedListAdapter {
                viewModel.reportUiEvent(event = PullRequestsUiEvent.ItemClick(pullRequest = it))
            }.apply {
                withLoadStateHeaderAndFooter(
                    header = PullRequestsLoadStateAdapter(retryLoadListener),
                    footer = PullRequestsLoadStateAdapter(retryLoadListener)
                )
                addLoadStateListener(loadStateListener)
            }.also { listAdapter = it }
        }
    }

    private fun attachListeners() {
        binding.toolbar.imgToolbarBackBtn.setOnClickListener {
            viewModel.reportUiEvent(event = PullRequestsUiEvent.ToolbarNavBtnClick)
        }
    }

    private fun addSubscriptions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { resolveUiState(it) }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pullRequestsFlow().flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collectLatest { listAdapter.submitData(it) }
        }
    }

    private fun resolveUiState(state: PullRequestsUiState) {
        displayUi(state)
        when (state) {
            is PullRequestsUiState.Default -> {
                bindToolbarUi(state)
            }
            is PullRequestsUiState.Empty -> {
                bindEmptyUi(state)
            }
            is PullRequestsUiState.Navigation -> {
                mainViewModel.escalateNavigation(navigation = state.navigation)
            }
            else -> {
                // noinspection: do nothing
            }
        }
    }

    private fun displayUi(state: PullRequestsUiState) {
        with(binding) {
            listPullRequests.isVisible = state is PullRequestsUiState.List
            txtPullRequestsListEmpty.isVisible = state is PullRequestsUiState.Empty
            progressBar.isVisible = state is PullRequestsUiState.Loading
        }
    }

    private fun bindToolbarUi(state: PullRequestsUiState.Default) {
        binding.toolbar.txtToolbarTitle.text = state.toolbarTitleText
    }

    private fun bindEmptyUi(state: PullRequestsUiState.Empty) {
        binding.txtPullRequestsListEmpty.text = state.pageText
    }

    private fun detachListeners() {
        binding.toolbar.imgToolbarBackBtn.setOnClickListener(null)
        listAdapter.removeLoadStateListener(loadStateListener)
    }
}