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
import androidx.paging.CombinedLoadStates
import androidx.recyclerview.widget.LinearLayoutManager
import com.navi.git.databinding.FragmentPullRequestsBinding
import com.navi.git.main.MainUiEvent
import com.navi.git.main.MainUiState
import com.navi.git.main.MainViewModel
import com.navi.git.main.adapter.PullRequestsLoadStateAdapter
import com.navi.git.main.adapter.PullRequestsPagedListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PullRequestsFragment : Fragment() {
    private var _binding: FragmentPullRequestsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var listAdapter: PullRequestsPagedListAdapter
    private val retryLoadListener: () -> Unit by lazy {
        { listAdapter.retry() }
    }
    private val loadStateListener: (CombinedLoadStates) -> Unit by lazy {
        { viewModel.reportUiEvent(event = MainUiEvent.LoadStateChange(it, listAdapter.itemCount)) }
    }

    /* Lifecycle */

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
        addSubscriptions()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listAdapter.removeLoadStateListener(loadStateListener)
        _binding = null
    }

    /* Operations */

    private fun configureRecycler() {
        with(binding.listPullRequests) {
            layoutManager = LinearLayoutManager(context)
            adapter = PullRequestsPagedListAdapter {
                viewModel.reportUiEvent(event = MainUiEvent.PullRequestItemClick(it))
            }.apply {
                withLoadStateHeaderAndFooter(
                    header = PullRequestsLoadStateAdapter(retryLoadListener),
                    footer = PullRequestsLoadStateAdapter(retryLoadListener)
                )
                addLoadStateListener(loadStateListener)
            }.also { listAdapter = it }
        }
    }

    private fun addSubscriptions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { resolveUiState(it) }
        }
    }

    private fun resolveUiState(state: MainUiState) {
        displayUi(state)
        when (state) {
            MainUiState.RetryListLoading -> retryLoadListener()
            is MainUiState.List -> bindListUi(state)
            else -> {
                // noinspection: do nothing
            }
        }
    }

    private fun displayUi(state: MainUiState) {
        binding.apply {
            txtPullRequestsListEmpty.isVisible = state is MainUiState.EmptyList
            listPullRequests.isVisible = state is MainUiState.List
        }
    }

    private fun bindListUi(state: MainUiState.List) {
        lifecycleScope.launchWhenStarted {
            listAdapter.submitData(state.pullRequests)
        }
    }
}