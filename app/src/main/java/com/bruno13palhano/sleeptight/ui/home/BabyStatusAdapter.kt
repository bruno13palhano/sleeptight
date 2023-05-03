package com.bruno13palhano.sleeptight.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bruno13palhano.model.BabyStatus
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.BabyStatusCardBinding
import com.bruno13palhano.sleeptight.ui.util.DateFormatUtil

class BabyStatusAdapter(
    private val onClick: (statusId: Long) -> Unit
) : ListAdapter<BabyStatus, BabyStatusAdapter.StatusItemViewHolder>(BabyStatusDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusItemViewHolder {
        val binding = BabyStatusCardBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return StatusItemViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: StatusItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class StatusItemViewHolder(
        private val binding: BabyStatusCardBinding,
        val onClick: (statusId: Long) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        var currentBabyStatus: BabyStatus? = null

        init {
            binding.root.setOnClickListener {
                currentBabyStatus?.let {
                    onClick(it.id)
                }
            }
        }

        fun bind(babyStatus: BabyStatus) {
            currentBabyStatus = babyStatus
            binding.title.text = babyStatus.title
            binding.date.text = DateFormatUtil.format(babyStatus.date)
            binding.height.text = itemView.resources.getString(R.string.height_tag, babyStatus.height)
            binding.weight.text = itemView.resources.getString(R.string.weight_tag, babyStatus.weight)
        }
    }

    private class BabyStatusDiffCallback : DiffUtil.ItemCallback<BabyStatus>() {
        override fun areItemsTheSame(oldItem: BabyStatus, newItem: BabyStatus): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BabyStatus, newItem: BabyStatus): Boolean {
            return newItem == oldItem
        }
    }
}