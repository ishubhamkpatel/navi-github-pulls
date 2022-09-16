package com.navi.git.features.pullrequests.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.navi.git.R
import com.navi.git.databinding.ItemPullRequestBinding
import com.navi.git.models.PullRequestUiModel
import com.navi.img.Shape
import com.navi.img.loadImage
import com.navi.logger.Logger

class PullRequestsPagedListAdapter(
    private inline val itemClickListener: (PullRequestUiModel) -> Unit
) : PagingDataAdapter<PullRequestUiModel, PullRequestsPagedListAdapter.ViewHolder>(DiffCallback) {
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<PullRequestUiModel>() {
            override fun areItemsTheSame(
                oldItem: PullRequestUiModel,
                newItem: PullRequestUiModel
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: PullRequestUiModel,
                newItem: PullRequestUiModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            binding = ItemPullRequestBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(pullRequest = it) }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            holder.bind(payloads[0] as PullRequestUiModel)
        }
    }

    inner class ViewHolder(
        private val binding: ItemPullRequestBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener { getItem(layoutPosition)?.let { itemClickListener(it) } }
        }

        fun bind(pullRequest: PullRequestUiModel) {
            binding.apply {
                imgPrUser.loadImage(
                    url = pullRequest.userImage,
                    placeholder = R.drawable.ic_launcher_foreground,
                    error = R.drawable.ic_launcher_foreground,
                    shape = Shape.Circle,
                    successListener = { Logger.d("Source = $it, Url = ${pullRequest.userImage}") },
                    failureListener = {
                        Logger.e(it ?: "Failed to load image, Url = ${pullRequest.userImage}")
                    }
                )
                txtPrTitle.text = pullRequest.title.orEmpty()
                txtPrUserName.text = pullRequest.userName.orEmpty()
                txtPrCreatedDt.text = pullRequest.createdDate.orEmpty()
                txtPrClosedDt.text = pullRequest.closedDate.orEmpty()
            }
        }
    }
}