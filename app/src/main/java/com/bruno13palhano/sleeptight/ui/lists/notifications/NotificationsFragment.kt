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
import com.bruno13palhano.sleeptight.databinding.FragmentNotificationsBinding
import com.bruno13palhano.sleeptight.ui.lists.notifications.receivers.NotificationReceiver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationsFragment : Fragment() {
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NotificationsViewModel by viewModels()
    private lateinit var notificationManager: NotificationManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_notifications, container, false)
        val view = binding.root

        binding.uiEvents = this

        notificationManager =
            activity?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val adapter = NotificationsAdapter(
            onItemClick = { navigateToNotification(it) },
            onDeleteItemClick = {
                cancelNotification(it.toInt())
                viewModel.deleteNotification(it)
            }
        )
        binding.notificationsList.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allNotifications.collect {
                    adapter.submitList(it)
                }
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToNotification(id: Long) {
        findNavController().navigate(
            NotificationsFragmentDirections.actionNotificationsToNotification(id))
    }

    fun navigateToNewNotification() {
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