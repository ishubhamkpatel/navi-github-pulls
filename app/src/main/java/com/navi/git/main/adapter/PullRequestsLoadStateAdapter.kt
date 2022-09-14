package com.navi.git.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.navi.git.databinding.ItemPullRequestsLoadStateBinding

class PullRequestsLoadStateAdapter(
    private inline val retryListener: () -> Unit
) : LoadStateAdapter<PullRequestsLoadStateAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        return ViewHolder(
            binding = ItemPullRequestsLoadStateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState = loadState)
    }

    inner class ViewHolder(
        private val binding: ItemPullRequestsLoadStateBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.txtRetryBtn.setOnClickListener { retryListener() }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                if (loadState is LoadState.Error) {
                    txtPullRequestsLoadError.text = loadState.error.localizedMessage
                }
                progressPullRequestsLoad.isVisible = loadState is LoadState.Loading
                txtRetryBtn.isVisible = loadState is LoadState.Error
                txtPullRequestsLoadError.isVisible = loadState is LoadState.Error
            }
        }
    }
}