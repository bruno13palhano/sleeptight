package com.bruno13palhano.sleeptight.ui.nap

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import com.bruno13palhano.model.Nap
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentNapBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class NapFragment : Fragment() {
    private var _binding: FragmentNapBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NapViewModel by viewModels()
    private var napId: Long = 0L

    private var date = 0L
    private var startTime = 0L
    private var endTime = 0L
    private var observation = ""
    private var currentDay = 1
    private var currentMonth = 1
    private var currentYear = 1
    private var currentStartHour = 1
    private var currentStartMinute = 1
    private var currentEndHour = 1
    private var currentEndMinute = 1

    private lateinit var datePicker: DatePickerDialog
    private lateinit var startTimePicker: TimePickerDialog
    private lateinit var endTimePicker: TimePickerDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_nap, container, false)
        val view = binding.root

        binding.lifecycleOwner = viewLifecycleOwner
        binding.uiEvents = this
        binding.viewModel = viewModel

        napId = NapFragmentArgs.fromBundle(requireArguments()).napId
        viewModel.getNap(napId)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.nap.collect {
                        date = it.date
                        startTime = it.startTime
                        endTime = it.endTime
                        observation = it.observation
                    }
                }
                launch {
                    viewModel.date.collect {
                        date = it
                        setDatePicker(it)
                    }
                }
                launch {
                    viewModel.startTime.collect {
                        startTime = it
                        setStartTimePicker(it)
                    }
                }
                launch {
                    viewModel.endTime.collect {
                        endTime = it
                        setEndTimePicker(it)
                    }
                }
                launch {
                    viewModel.observation.collect {
                        observation = it
                    }
                }
            }
        }

        datePicker = DatePickerDialog(requireContext(), { _, year, monthOfYear, day ->
            viewModel.setDate(year, monthOfYear, day)
        }, currentYear, currentMonth, currentDay)

        startTimePicker = TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
            viewModel.setStartTime(hourOfDay, minute)
        }, currentStartHour, currentStartMinute, true)

        endTimePicker = TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
            viewModel.setEndTime(hourOfDay, minute)
        }, currentEndHour, currentEndMinute, true)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun updatePath() {
        findNavController().navigateUp()
        viewModel.updateNap(
            Nap(
                id = napId,
                date = date,
                startTime = startTime,
                endTime = endTime,
                observation = observation
            )
        )
    }

    fun onDateClick() {
        datePicker.updateDate(currentYear, currentMonth, currentDay)
        datePicker.show()
    }

    fun onStartTimeClick() {
        startTimePicker.updateTime(currentStartHour, currentStartMinute)
        startTimePicker.show()
    }

    fun onEndTimeClick() {
        endTimePicker.updateTime(currentEndHour, currentEndMinute)
        endTimePicker.show()
    }

    private fun setDatePicker(date: Long) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        currentYear = calendar.get(Calendar.YEAR)
        currentMonth = calendar.get(Calendar.MONTH)
        currentDay = calendar.get(Calendar.DAY_OF_MONTH)
    }

    private fun setStartTimePicker(time: Long) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        currentStartHour = calendar.get(Calendar.HOUR_OF_DAY)
        currentStartMinute = calendar.get(Calendar.MINUTE)
    }

    private fun setEndTimePicker(time: Long) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        currentEndHour = calendar.get(Calendar.HOUR_OF_DAY)
        currentEndMinute = calendar.get(Calendar.MINUTE)
    }
}