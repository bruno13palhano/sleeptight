package com.bruno13palhano.sleeptight.ui.lists.notifications.newnotification

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
import com.bruno13palhano.sleeptight.databinding.FragmentNewNotificationBinding
import com.bruno13palhano.sleeptight.ui.lists.notifications.AlarmNotification
import com.bruno13palhano.sleeptight.ui.lists.notifications.receivers.NotificationReceiver
import com.bruno13palhano.sleeptight.ui.util.TimePickerUtil
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewNotificationFragment : Fragment() {
    private var _binding: FragmentNewNotificationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewNotificationViewModel by viewModels()

    private lateinit var datePicker: MaterialDatePicker<Long>
    private lateinit var timePicker: MaterialTimePicker

    private var title: String = ""
    private var description: String = ""
    private var date: Long = 0L
    private var time: Long = 0L
    private var repeat: Boolean = false

    private lateinit var notificationManager: NotificationManager
    private lateinit var alarmManager: AlarmManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_new_notification, container, false)
        val view = binding.root

        binding.uiEvents = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.hour.collect {
                        setTimePicker(it)
                        time = it
                    }
                }
                launch {
                    viewModel.date.collect {
                        setDatePicker(it)
                        date = it
                    }
                }
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
                    viewModel.repeat.collect {
                        repeat = it
                    }
                }
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun onActionClick() {
        viewModel.insertNotification()
        setAlarm()
    }

    fun onTimeClick() {
        if (!timePicker.isAdded)
            timePicker.show(requireParentFragment().parentFragmentManager, "time_dialog")
    }

    fun onDateClick() {
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

    private fun setTimePicker(time: Long) {
        timePicker = TimePickerUtil.prepareTimePicker(time)
        timePicker.addOnPositiveButtonClickListener {
            viewModel.setHour(timePicker.hour, timePicker.minute)
        }
    }

    private fun setAlarm() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getLastNotification().collect {
                notificationManager =
                    activity?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

                val notifyIntent = Intent(requireContext(), NotificationReceiver::class.java)
                notifyIntent.apply {
                    putExtra("id", it.id.toInt())
                    putExtra("title", title)
                    putExtra("description", description)
                }

                val notifyPendingIntent = PendingIntent.getBroadcast(
                    requireContext(),
                    it.id.toInt(),
                    notifyIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                alarmManager = requireContext().getSystemService(ALARM_SERVICE) as AlarmManager

                val alarmNotification = AlarmNotification(
                    notificationManager = notificationManager,
                    alarmManager = alarmManager
                )

                alarmNotification.setAlarmManager(
                    notifyPendingIntent = notifyPendingIntent,
                    time = time,
                    date = date,
                    repeat = repeat
                )

                findNavController().navigateUp()
            }
        }
    }
}