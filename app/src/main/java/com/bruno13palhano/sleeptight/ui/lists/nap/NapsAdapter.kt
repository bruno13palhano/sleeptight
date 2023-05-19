package com.bruno13palhano.sleeptight.ui.lists.nap

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bruno13palhano.model.Nap
import com.bruno13palhano.sleeptight.databinding.BabyStatusCardBinding
import com.bruno13palhano.sleeptight.ui.util.DateFormatUtil

class NapsAdapter(
    private val onClick: (napId: Long) -> Unit
) : ListAdapter<Nap, NapsAdapter.NapItemViewHolder>(PathDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NapItemViewHolder {
        val binding = BabyStatusCardBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return NapItemViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: NapItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class NapItemViewHolder(
        private val binding: BabyStatusCardBinding,
        val onClick: (napId: Long) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private var currentNap: Nap? = null

        init {
            binding.root.setOnClickListener {
                currentNap?.let {
                    onClick(it.id)
                }
            }
        }

        fun bind(nap: Nap) {
            currentNap = nap
            binding.title.text = nap.title
            binding.date.text = DateFormatUtil.format(nap.date)
        }
    }

    private class PathDiffCallback : DiffUtil.ItemCallback<Nap>() {
        override fun areItemsTheSame(oldItem: Nap, newItem: Nap): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Nap, newItem: Nap): Boolean {
            return newItem == oldItem
        }
    }
}