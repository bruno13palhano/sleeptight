package com.bruno13palhano.sleeptight.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bruno13palhano.model.Nap
import com.bruno13palhano.sleeptight.databinding.NapCardBinding

class NapsAdapter(
    private val onClick: (napId: Long) -> Unit
) : ListAdapter<Nap, NapsAdapter.NapItemViewHolder>(PathDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NapItemViewHolder {
        val binding = NapCardBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return NapItemViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: NapItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class NapItemViewHolder(
        private val binding: NapCardBinding,
        val onClick: (napId: Long) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        var currentNap: Nap? = null

        init {
            binding.root.setOnClickListener {
                currentNap?.let {

                }
            }
        }

        fun bind(nap: Nap) {

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