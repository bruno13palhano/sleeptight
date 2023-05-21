package com.bruno13palhano.sleeptight.ui.lists.notifications

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentCommonListBinding
import com.bruno13palhano.sleeptight.ui.lists.notifications.receivers.NotificationReceiver
import com.bruno13palhano.sleeptight.ui.lists.CommonListView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationsFragment : Fragment(), CommonListView {
    private var _binding: FragmentCommonListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NotificationsViewModel by viewModels()
    private lateinit var notificationManager: NotificationManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_common_list, container, false)
        val view = binding.root

        notificationManager =
            activity?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val adapter = NotificationsAdapter(
            onItemClick = { onListItemClick(it) },
            onDeleteItemClick = {
                cancelNotification(it.toInt())
                viewModel.deleteNotification(it)
            }
        )
        binding.list.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allNotifications.collect {
                    adapter.submitList(it)
                }
            }
        }

        binding.addButton.setOnClickListener {
            onAddItemClick()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onListItemClick(itemId: Long) {
        findNavController().navigate(
            NotificationsFragmentDirections.actionNotificationsToNotification(itemId))
    }

    override fun onAddItemClick() {
        findNavController().navigate(
            NotificationsFragmentDirections.actionNotificationsToNewNotification())
    }

    private fun cancelNotification(notificationId: Int) {
        val notifyIntent = Intent(requireContext(), NotificationReceiver::class.java)
        notifyIntent.apply {
            putExtra("id", notificationId)
        }

        val notifyPendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            notificationId,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        val alarmManager = requireContext().getSystemService(ALARM_SERVICE) as AlarmManager

        val alarmNotification = AlarmNotification(
            notificationManager = notificationManager,
            alarmManager = alarmManager
        )

        alarmNotification.cancelNotification(notifyPendingIntent, notificationId)
    }
}