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
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentNotificationBinding
import com.bruno13palhano.sleeptight.ui.lists.notifications.receivers.NotificationReceiver
import com.bruno13palhano.sleeptight.ui.util.TimePickerUtil
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationFragment : Fragment(), NotificationView {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NotificationViewModel by viewModels()
    private var notificationId: Long = 0L
    private var title: String = ""
    private var description: String = ""
    private var time: Long = 0L
    private var date: Long = 0L
    private var repeat: Boolean = false

    private lateinit var datePicker: MaterialDatePicker<Long>
    private lateinit var timePicker: MaterialTimePicker
    private lateinit var notificationManager: NotificationManager
    private lateinit var alarmManager: AlarmManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_notification, container, false)
        val view = binding.root

        binding.uiEvents = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        notificationId = NotificationFragmentArgs.fromBundle(requireArguments()).notificationId

        viewModel.setNotification(notificationId)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.title.collect {
                        title = it
                    }
                }
                launch {
                    viewModel.description.collect {
                        description = it
                    }
                }
                launch {
                    viewModel.hour.collect {
                        time = it
                        setTimepicker(it)
                    }
                }
                launch {
                    viewModel.date.collect {
                        date = it
                        setDatePicker(it)
                    }
                }
                launch {
                    viewModel.repeat.collect {
                        repeat = it
                    }
                }
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.common_item_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.delete -> {
                        true
                    }
                    R.id.share -> {
                        true
                    }
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActionClick() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.updateNotification(notificationId)
        }
        updateAlarm()
    }

    override fun onTimeClick() {
        if (!timePicker.isAdded)
            timePicker.show(requireParentFragment().parentFragmentManager, "hour_dialog")
    }

    override fun onDateClick() {
        if (!datePicker.isAdded)
            datePicker.show(requireParentFragment().parentFragmentManager, "date_dialog")
    }

    private fun setDatePicker(date: Long) {
        datePicker = MaterialDatePicker
            .Builder
            .datePicker()
            .setSelection(date)
            .build()
        datePicker.addOnPositiveButtonClickListener {
            viewModel.setDate(it)
        }
    }

    private fun setTimepicker(time: Long) {
        timePicker = TimePickerUtil.prepareTimePicker(time)
        timePicker.addOnPositiveButtonClickListener {
            viewModel.setHour(timePicker.hour, timePicker.minute)
        }
    }

    private fun updateAlarm() {
        notificationManager =
            activity?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val notifyIntent = Intent(requireContext(), NotificationReceiver::class.java)

        notifyIntent.apply {
            putExtra("id", notificationId.toInt())
            putExtra("title", title)
            putExtra("description", description)
        }

        val notifyPendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            notificationId.toInt(),
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager = requireContext().getSystemService(ALARM_SERVICE) as AlarmManager

        val alarmNotification = AlarmNotification(
            notificationManager = notificationManager,
            alarmManager = alarmManager
        )

        alarmNotification.updateAlarmManager(
            notifyPendingIntent = notifyPendingIntent,
            notificationId = notificationId.toInt(),
            time = time,
            date = date,
            repeat = repeat
        )

        findNavController().navigateUp()
    }
}