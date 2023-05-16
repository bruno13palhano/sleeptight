package com.bruno13palhano.sleeptight.ui.lists.notifications

import android.icu.text.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bruno13palhano.model.Notification
import com.bruno13palhano.sleeptight.databinding.NotificationCardBinding
import com.bruno13palhano.sleeptight.ui.util.DateFormatUtil

class NotificationsAdapter(
    private val onItemClick: (id: Long) -> Unit,
    private val onDeleteItemClick: (id: Long) -> Unit
) : ListAdapter<Notification, NotificationsAdapter.NotificationItemViewHolder>(NotificationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationItemViewHolder {
        val binding = NotificationCardBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationItemViewHolder(binding, onItemClick, onDeleteItemClick)
    }

    override fun onBindViewHolder(holder: NotificationItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class NotificationItemViewHolder(
        private val binding: NotificationCardBinding,
        val onItemClick: (id: Long) -> Unit,
        val onDeleteItemClick: (id: Long) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private var currentNotification: Notification? = null

        init {
            binding.root.setOnClickListener {
                currentNotification?.let {
                    onItemClick(it.id)
                }
            }

            binding.delete.setOnClickListener {
                currentNotification?.let {
                    onDeleteItemClick(it.id)
                }
            }
        }

        fun bind(notification: Notification) {
            currentNotification = notification
            binding.title.text = notification.title
            binding.hour.text = DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE)
                .format(notification.time)
            binding.date.text = DateFormatUtil.format(notification.date)
        }
    }

    private class NotificationDiffCallback : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem == newItem
        }
    }
}